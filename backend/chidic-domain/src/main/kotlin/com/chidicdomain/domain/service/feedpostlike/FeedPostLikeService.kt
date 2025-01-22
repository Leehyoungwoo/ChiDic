package com.chidicdomain.domain.service.feedpostlike

import com.chidiccommon.dto.FeedLikeResponse

interface FeedPostLikeService {
    fun getLikeCount(feedPostId: Long): FeedLikeResponse
    fun likeFeedPost(userId: Long, feedPostId: Long)
    fun unlikeFeedPost(userId: Long, feedPostId: Long)
}