package com.chidicdomain.domain.service.feedpost

import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto

interface FeedPostService {
    fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long?, size: Int, start: Long): List<FeedPostListDto>
    fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto
    fun createFeed(feedPostCreateDto: FeedPostCreateDto)
    fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto)
    fun deleteFeedPost(feedPostId: Long)
}

class FeedPostNotFoundException(message: String) : RuntimeException(message)