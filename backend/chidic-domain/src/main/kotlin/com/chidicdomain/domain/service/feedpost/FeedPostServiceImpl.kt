package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccommon.exception.ExceptionMessage.*
import com.chidiccommon.exception.exceptions.FeedPostNotFoundException
import com.chidicdomain.domain.mapper.feedpost.FeedPostMapper
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostMapper: FeedPostMapper
): FeedPostService {
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
}