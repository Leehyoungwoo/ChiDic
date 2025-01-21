package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostUpdateRequest

interface FeedPostService {
    fun getFeedPostDetail(feedPostId: Long): FeedPostDetailResponse
    fun createFeed(userId: Long, feedPostCreateRequest: FeedPostCreateRequest)
    fun updateFeed(feedPostId: Long, feedPostUpdateRequest: FeedPostUpdateRequest)
    fun deleteFeedPost(feedPostId: Long)
}