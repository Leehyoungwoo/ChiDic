package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccommon.exception.ExceptionMessage.*
import com.chidiccommon.exception.exceptions.FeedPostNotFoundException
import com.chidiccommon.exception.exceptions.UserNotFoundException
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.mapper.feedpost.FeedPostMapper
import com.chidicdomain.domain.repository.FeedPostLikeRepository
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.FollowRepository
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.redis.service.RedisService
import org.springframework.data.domain.Page
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
    private val feedPostLikeRepository: FeedPostLikeRepository,
    private val redisService: RedisService
) : FeedPostService {
    override fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long?, size: Int, start: Long): List<FeedPostListResponse> {
        val user = userRepository.getReferenceById(userId)
        val pageable = if (lastFeedPostId == null) {
            PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"))
        } else {
            PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"))
        }
        println("사용자 PK " + userId)

        val followList = followRepository.findAllByFollower(user)

        val userList = followList.map { it.followee!! }

        val cachedFeedPosts = mutableListOf<FeedPost>()

        userList.forEach { followee ->
            val cachedPosts = redisService.getFeedPosts(followee.id, start, start + size)
            if (cachedPosts.isNotEmpty()) {
                // 캐시에서 피드를 가져왔다면 리스트에 추가
                cachedFeedPosts.addAll(cachedPosts)
            }
        }

        if (cachedFeedPosts.isEmpty()) {
            val postsFromDb = feedPostRepository.findByUserIn(userList, pageable)
            postsFromDb.forEach {
                redisService.saveFeedPost(it.user.id, it)
            }
            cachedFeedPosts.addAll(postsFromDb)
        }

        val filteredPosts = cachedFeedPosts
            .filter { lastFeedPostId == null || it.id < lastFeedPostId }
            .sortedByDescending { it.createdAt }

        // 4. 요청 크기(size)만큼 슬라이싱
        val paginatedPosts = filteredPosts.take(size)

        // 5. Response로 변환
        return paginatedPosts.map { feedPost ->
            FeedPostListResponse(
                feedPostId = feedPost.id,
                title = feedPost.title,
                content = feedPost.content,
                writer = feedPost.user.username,
                writerProfile = feedPost.user.profilePicture,
                likeCount = feedPostLikeRepository.countByFeedPost(feedPost),
                commentCont = feedPost.comments.size.toLong(),
                createdAt = feedPost.createdAt
            )
        }
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailResponse {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        return feedPostMapper.toFeedPostDetailResponse(feedPost)
    }

    @Transactional
    override fun createFeed(userId: Long, feedPostCreateRequest: FeedPostCreateRequest) {
        val proxyUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }

        val newFeedPost = feedPostMapper.toEntity(proxyUser, feedPostCreateRequest)

        val feedPost = feedPostRepository.save(newFeedPost)

        redisService.saveFeedPost(userId, feedPost)
    }

    @Transactional
    override fun updateFeed(feedPostId: Long, feedPostUpdateRequest: FeedPostUpdateRequest) {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.updateFeedPost(feedPostUpdateRequest)
    }

    @Transactional
    override fun deleteFeedPost(feedPostId: Long) {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.deleteData()
    }
}