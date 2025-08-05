package com.chidic.domain.service.feedpostlike

import com.chidic.dto.FeedLikeDto

interface FeedPostLikeService {
    fun getLikeCount(feedPostId: Long): FeedLikeDto
    fun likeFeedPost(userId: Long, feedPostId: Long)
    fun createLikeAndIncrementCount(userId: Long, feedPostId: Long): Boolean
    fun unlikeFeedPost(userId: Long, feedPostId: Long)
    fun decrementCount(userId: Long, feedPostId: Long): Boolean
}