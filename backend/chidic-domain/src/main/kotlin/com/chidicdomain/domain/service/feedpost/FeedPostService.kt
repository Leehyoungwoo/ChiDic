package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import org.springframework.data.domain.Page

interface FeedPostService {
    fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long?, size: Int, start: Long): List<FeedPostListResponse>
    fun getFeedPostDetail(feedPostId: Long): FeedPostDetailResponse
    fun createFeed(userId: Long, feedPostCreateRequest: FeedPostCreateRequest)
    fun updateFeed(feedPostId: Long, feedPostUpdateRequest: FeedPostUpdateRequest)
    fun deleteFeedPost(feedPostId: Long)
}