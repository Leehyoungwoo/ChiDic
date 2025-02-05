package com.chidicdomain.redis.service

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.dto.FeedPostListDto
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
) : RedisService {

    init {
        objectMapper.registerModule(JavaTimeModule())
    }

    override fun saveFeedPost(userId: Long, feedPostListDto: FeedPostListDto) {
        val redisKey = getKey(userId)
        val score = -feedPostListDto.feedPostId.toDouble()
        redisTemplate.opsForZSet().add(redisKey, feedPostListDto.feedPostId.toString(), score)

        savaFeedPostDtoToHash(feedPostListDto)
    }

    override fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost> {
        val redisKey = getKey(userId)
        val feedPostsJson = redisTemplate.opsForZSet().reverseRange(redisKey, start, end) ?: emptySet()
        return feedPostsJson.map { json ->
            objectMapper.readValue(json, FeedPost::class.java)
        }
    }


    override fun getFeedPostIdsForUser(userId: Long, lastFeedPostId: Long?, size: Int): List<Long> {
        val key = getKey(userId)

        // 첫 페이지 조회 시 처음부터 가져오기
        val startIndex = if (lastFeedPostId == null) 0
        else (redisTemplate.opsForZSet().rank(key, lastFeedPostId.toString())?.plus(1)
            ?: 0) // ZSET에서 lastFeedPostId의 위치를 찾고 다음부터 조회

        val endIndex = startIndex + size - 1

        // Redis에서 feedPostId 리스트 가져오기 (정렬 순서는 저장될 때 PK 내림차순이므로 ZRANGE 사용)
        val feedPostIds = redisTemplate.opsForZSet()
            .range(key, startIndex, endIndex) ?: emptySet()

        return feedPostIds.map { it.toLong() }
    }

    override fun getFeedPostsFromHash(feedPostIds: List<Long>): List<FeedPostListDto> {
        return feedPostIds.mapNotNull { feedPostId ->
            val key = getHashKey(feedPostId)
            val json = redisTemplate.opsForValue().get(key) ?: return@mapNotNull null
            objectMapper.readValue(json, FeedPostListDto::class.java)
        }
    }

    override fun setExpiration(key: String, duration: Duration) {
        redisTemplate.expire(key, duration)
    }

    private fun getKey(userId: Long) = "user:feed:$userId"

    private fun getHashKey(feedPostId: Long) = "feedpost:details:$feedPostId"

    private fun savaFeedPostDtoToHash(feedPostListDto: FeedPostListDto) {
        val key = getHashKey(feedPostListDto.feedPostId)
        val json = objectMapper.writeValueAsString(feedPostListDto)

        redisTemplate.opsForValue().set(key, json)
        setExpiration(key, Duration.ofDays(7))
    }
}
