package com.chidic.domain.service.feedpost

import com.chidic.dto.FeedPostCreateDto
import com.chidic.dto.FeedPostDetailDto
import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto

interface FeedPostService {
    fun getFollowedUsersFeed(userId: Long, lastFeedPostId: Long, size: Int, start: Long): List<FeedPostListDto>
    fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto
    fun createFeed(feedPostCreateDto: FeedPostCreateDto)
    fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto)
    fun deleteFeedPost(feedPostId: Long)
}

class FeedPostNotFoundException(message: String) : RuntimeException(message)