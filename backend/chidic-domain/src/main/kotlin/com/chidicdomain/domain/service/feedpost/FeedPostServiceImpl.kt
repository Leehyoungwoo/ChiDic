package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccommon.exception.ExceptionMessage.*
import com.chidiccommon.exception.exceptions.FeedPostNotFoundException
import com.chidicdomain.domain.mapper.feedpost.FeedPostMapper
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.FollowRepository
import com.chidicdomain.domain.repository.UserRepository
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
    private val followRepository: FollowRepository
) : FeedPostService {
    override fun getFollowedUsersFeed(userId: Long, page: Int, size: Int): List<FeedPostListResponse> {
        val user = userRepository.getReferenceById(userId)
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))

        val followList = followRepository.findAllByFollower(user)
        val userList = followList.map { it.followee!! }

        val feedPosts = feedPostRepository.findByUserIn(userList, pageable)

        return feedPosts.map { feedPost ->
            FeedPostListResponse(
                feedPostId = feedPost.id,
                title = feedPost.title,
                content = feedPost.content,
                writer = feedPost.user.username,
                writerProfile = feedPost.user.profilePicture,
                likeCount = 0L,
                commentCont = feedPost.comments.size.toLong(),
                createdAt = feedPost.createdAt
            )
        }.toList()
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailResponse {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        return feedPostMapper.toFeedPostDetailResponse(feedPost)
    }

    @Transactional
    override fun createFeed(userId: Long, feedPostCreateRequest: FeedPostCreateRequest) {
        val proxyUser = userRepository.getReferenceById(userId)
        val newFeedPost = feedPostMapper.toEntity(proxyUser, feedPostCreateRequest)
        feedPostRepository.save(newFeedPost)
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