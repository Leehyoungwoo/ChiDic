package com.chidicdomain.domain.service.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostUpdateDto

interface FeedPostService {
    fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long?, size: Int, start: Long): List<FeedPostListResponse>
    fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto
    fun createFeed(feedPostCreateDto: FeedPostCreateDto)
    fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto)
    fun deleteFeedPost(feedPostId: Long)
}