package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostUpdateRequest

interface FeedPostService {
    fun createFeed(userId: Long, feedPostCreateRequest: FeedPostCreateRequest)
    fun updateFeed(feedPostId: Long, feedPostUpdateRequest: FeedPostUpdateRequest)
}