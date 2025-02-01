package com.chidicdomain.domain.service.feedpostlike

import com.chidicdomain.dto.FeedLikeDto

interface FeedPostLikeService {
    fun getLikeCount(feedPostId: Long): FeedLikeDto
    fun likeFeedPost(userId: Long, feedPostId: Long)
    fun unlikeFeedPost(userId: Long, feedPostId: Long)
}