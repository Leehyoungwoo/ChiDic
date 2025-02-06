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
        // Step 1️⃣: Redis에서 feedPostId 리스트 조회
        val cachedFeedPostIds = redisService.getFeedPostIdsForUser(userId, lastFeedPostId, size)

        // Step 2️⃣: feedPostId 리스트 자체가 캐시 미스일 경우 → DB에서 가져오기
        if (cachedFeedPostIds.isEmpty()) {
            val followList = followRepository.findAllByFollower(userRepository.getReferenceById(userId))
            val userList = followList.map { it.followee!! }

            val postsFromDb = feedPostRepository.findByUserIn(
                userList,
                PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"))
            )

            // Redis에 feedPostId 저장 & 개별 게시글 캐싱
            postsFromDb.forEach {
                val dto = feedPostMapper.toFeedPostListDto(it)
                redisService.saveFeedPost(userId, dto) // feedPostId 저장
            }

            return postsFromDb.map { feedPostMapper.toFeedPostListDto(it) }
        }

        // Step 3️⃣: 개별적으로 Redis에서 feedPostDto 조회 + 캐시 미스 처리
        val (cachedFeedPosts, missingFeedPostIds) = cachedFeedPostIds
            .map { it to redisService.getFeedPostFromHash(it) } // 개별 조회
            .partition { it.second != null } // 캐시 히트/미스 분리

        // Step 4️⃣: Redis에서 가져온 데이터 정리
        val finalFeedPosts = cachedFeedPosts.map { it.second!! }.toMutableList()

        // Step 5️⃣: 캐시 미스 난 경우, DB에서 조회 후 Redis에 저장
        if (missingFeedPostIds.isNotEmpty()) {
            val missingFeedPosts = feedPostRepository.findAllById(missingFeedPostIds.map { it.first })

            missingFeedPosts.forEach { feedPost ->
                val dto = feedPostMapper.toFeedPostListDto(feedPost)
                redisService.savaFeedPostDtoToHash(dto) // Redis에 캐싱
                finalFeedPosts.add(dto)
            }
        }

        // 읽은 피드는 읽음 처리(제거)
        redisService.markReadAsFeed(userId, cachedFeedPostIds)

        return finalFeedPosts.sortedByDescending { it.feedPostId }
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto {
        val feedPost = feedPostRepository.findFeedPostWithUserAndComments(feedPostId)
            ?: throw FeedPostNotFoundException(FEED_POST_NOT_FOUND.message)
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

        redisService.updateFeed(feedPostUpdateDto)
    }

    @Transactional
    override fun deleteFeedPost(feedPostId: Long) {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.deleteData()
    }
}

