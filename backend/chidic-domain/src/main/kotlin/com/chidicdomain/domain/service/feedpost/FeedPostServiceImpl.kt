package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.exception.ExceptionMessage.FEED_POST_NOT_FOUND
import com.chidicdomain.domain.mapper.feedpost.FeedPostMapper
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.FollowRepository
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto
import com.chidicdomain.redis.service.RedisService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostMapper: FeedPostMapper,
    private val followRepository: FollowRepository,
    private val redisService: RedisService
) : FeedPostService {
    override fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long?, size: Int, start: Long): List<FeedPostListDto> {
        // Redis에서 feedPostId 리스트 조회
        val cachedFeedPostIds = redisService.getFeedPostIdsForUser(userId, lastFeedPostId, size)

        // Redis에서 가져온 ID가 없으면 DB에서 조회 (캐싱 미스)
        if (cachedFeedPostIds.isEmpty()) {
            val followList = followRepository.findAllByFollower(userRepository.getReferenceById(userId))
            val userList = followList.map { it.followee!! }

            val postsFromDb = feedPostRepository.findByUserIn(
                userList,
                PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"))
            )

            // Redis 캐싱
            postsFromDb.forEach {
                redisService.saveFeedPost(userId, feedPostMapper.toFeedPostListDto(it))
            }

            return postsFromDb.map { feedPostMapper.toFeedPostListDto(it) }
        }

        // DTO 변환 후 반환
        return redisService.getFeedPostsFromHash(cachedFeedPostIds)
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto {
        val feedPost = feedPostRepository.findFeedPostWithUserAndComments(feedPostId)
        return feedPostMapper.toFeedPostDetailDto(feedPost)
    }

    @Transactional
    override fun createFeed(feedPostCreateDto: FeedPostCreateDto) {
        val proxyUser = userRepository.getReferenceById(feedPostCreateDto.userId)

        val newFeedPost = feedPostMapper.toEntity(proxyUser, feedPostCreateDto)

        feedPostRepository.save(newFeedPost)

        // 팔로우하고 있는 userId에 sorted Set들에 게시물을 저장
        // Reids Hash에 Dto로 변환하여 Redis로 넘겨야 함
        // Redis는 캐싱 역할만 담당
        val followers = followRepository.findAllByFollowee(proxyUser)
        val feedPostListDto = feedPostMapper.toFeedPostListDto(newFeedPost)

        followers.forEach{ follow ->
            redisService.saveFeedPost(follow.follower!!.id, feedPostListDto)
        }
    }

    @Transactional
    override fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto) {
        val feedPost = feedPostRepository.findById(feedPostUpdateDto.feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.updateFeedPost(feedPostUpdateDto)
    }

    @Transactional
    override fun deleteFeedPost(feedPostId: Long) {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.deleteData()
    }
}

