package com.chidic.redis.service

import com.chidic.domain.entity.FeedPost
import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import java.time.Duration

interface RedisService {
    fun saveFeedPost(userId: Long, feedPostListDto: FeedPostListDto)
    fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost>
    fun setExpiration(key: String, duration: Duration)
    fun getFeedPostIdsForUser(userId: Long, lastFeedPostId: Long?, size: Int) : List<Long>
    fun getFeedPostFromHash(feedPostId: Long): FeedPostListDto?
    fun saveFeedPostDtoToHash(feedPostListDto: FeedPostListDto)
    fun updateLikeCount(feedPostId: Long, newLikeCount: Int)
    fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto)
    fun markReadAsFeed(userId: Long, readFeedPostIds: List<Long>)
    fun getReadMarkList(userId: Long): List<Long>
    fun setIfNotExist(key: String, value: String, ttlInSeconds: Long): Boolean
}