package com.chidicdomain.domain.service.feedpostlike

interface FeedPostLikeService {
    fun likeFeedPost(userId: Long, feedPostId: Long)
}