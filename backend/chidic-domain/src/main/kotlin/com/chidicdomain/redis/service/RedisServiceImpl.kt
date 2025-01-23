package com.chidicdomain.redis.service

import com.chidicdomain.domain.entity.FeedPost
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.ZoneOffset

@Service
class RedisServiceImpl(
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
): RedisService {

    init {
        objectMapper.registerModule(JavaTimeModule())
    }

    override fun saveFeedPost(userId: Long, feedPost: FeedPost) {
        val redisKey = getKey(userId)
        val feedPostJson = objectMapper.writeValueAsString(feedPost)
        val score = feedPost.createdAt.toEpochSecond(ZoneOffset.UTC)
        redisTemplate.opsForZSet().add(redisKey, feedPostJson, score.toDouble())
    }

    override fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost> {
        val redisKey = getKey(userId)
        val feedPostsJson = redisTemplate.opsForZSet().range(redisKey, start, end) ?: emptySet()
        return feedPostsJson.map { json ->
            objectMapper.readValue(json, FeedPost::class.java)
        }
    }

    override fun setExpiration(key: String, duration: Duration) {
        redisTemplate.expire(key, duration)
    }

    private fun getKey(userId: Long) = "user:feed:$userId"
}
