package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.exception.ExceptionMessage.FEED_POST_NOT_FOUND
import com.chidiccommon.exception.ExceptionMessage.USER_NOT_FOUND
import com.chidicdomain.domain.mapper.feedpost.FeedPostMapper
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.FollowRepository
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.domain.service.user.UserNotFoundException
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
        val cachedFeedPost = redisService.getFeedPostsForUser(userId, lastFeedPostId, size)

        if (cachedFeedPost.isEmpty()) {
            val followList = followRepository.findAllByFollower(userRepository.getReferenceById(userId))
            val userList = followList.map { it.followee!! }
            val postsFromDb = feedPostRepository.findByUserIn(userList, PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id")))
            postsFromDb.forEach { redisService.saveFeedPost(userId, it) }
            return postsFromDb.map { feedPostMapper.toFeedPostListDto(it) }
        }

        return cachedFeedPost.map { feedPostMapper.toFeedPostListDto(it) }
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto {
        val feedPost = feedPostRepository.findFeedPostWithUserAndComments(feedPostId)
        return feedPostMapper.toFeedPostDetailDto(feedPost)
    }

    @Transactional
    override fun createFeed(feedPostCreateDto: FeedPostCreateDto) {
        val proxyUser = userRepository.findById(feedPostCreateDto.userId)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }

        val newFeedPost = feedPostMapper.toEntity(proxyUser, feedPostCreateDto)

        feedPostRepository.save(newFeedPost)

        // 팔로우하고 있는 userId에 sorted Set들에 게시물을 저장
        val followers = followRepository.findAllByFollowee(proxyUser)
        followers.forEach{ follow ->
            redisService.saveFeedPost(follow.follower!!.id, newFeedPost)
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

