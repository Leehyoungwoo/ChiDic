package com.chidicdomain.redis.service

import com.chidicdomain.domain.entity.FeedPost
import java.time.Duration

interface RedisService {
    fun saveFeedPost(userId: Long, feedPost: FeedPost)
    fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost>
    fun setExpiration(key: String, duration: Duration)
    fun getFeedPostIdsForUser(userId: Long, lastFeedPostId: Long?, size: Int) : List<Long>
}