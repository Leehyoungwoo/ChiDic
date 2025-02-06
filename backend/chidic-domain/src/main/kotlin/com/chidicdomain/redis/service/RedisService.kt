package com.chidicdomain.redis.service

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto
import java.time.Duration

interface RedisService {
    fun saveFeedPost(userId: Long, feedPostListDto: FeedPostListDto)
    fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost>
    fun setExpiration(key: String, duration: Duration)
    fun getFeedPostIdsForUser(userId: Long, lastFeedPostId: Long?, size: Int) : List<Long>
    fun getFeedPostFromHash(feedPostId: Long): FeedPostListDto?
    fun savaFeedPostDtoToHash(feedPostListDto: FeedPostListDto)
    fun updateLikeCount(feedPostId: Long, newLikeCount: Int)
    fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto)
    fun markReadAsFeed(userId: Long, readFeedPostIds: List<Long>)
}