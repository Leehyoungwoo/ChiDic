package com.chidicdomain.redis.service

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisServiceImpl(
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) : RedisService {

    init {
        objectMapper.registerModule(JavaTimeModule())
    }

    /**
     * 뉴스 피드 저장
     */
    override fun saveFeedPost(userId: Long, feedPostListDto: FeedPostListDto) {
        val redisKey = getKey(userId)
        val score = System.currentTimeMillis().toDouble()

        redisTemplate.opsForZSet().add(redisKey, feedPostListDto.feedPostId.toString(), score)
        saveFeedPostDtoToHash(feedPostListDto)

        // ZSET 크기 제한 (1000개 유지)
        val maxSize = 1000L
        val currentSize = redisTemplate.opsForZSet().size(redisKey) ?: 0L
        if (currentSize > maxSize) {
            val removeCount = currentSize - maxSize
            redisTemplate.opsForZSet().removeRange(redisKey, 0, removeCount - 1)
        }
    }

    /**
     * 뉴스 피드 조회
     */
    override fun getFeedPosts(userId: Long, start: Long, end: Long): List<FeedPost> {
        val redisKey = getKey(userId)
        val feedPostsJson = redisTemplate.opsForZSet().reverseRange(redisKey, start, end) ?: emptySet()

        return feedPostsJson.map { json -> objectMapper.readValue(json, FeedPost::class.java) }
    }

    /**
     * 뉴스 피드 ID 목록 조회
     */
    override fun getFeedPostIdsForUser(userId: Long, lastFeedPostId: Long?, size: Int): List<Long> {
        val key = getKey(userId)

        val startIndex = if (lastFeedPostId == null) 0
        else (redisTemplate.opsForZSet().reverseRank(key, lastFeedPostId.toString())?.plus(1) ?: 0)

        val endIndex = startIndex + size - 1
        val feedPostIds = redisTemplate.opsForZSet().reverseRange(key, startIndex, endIndex) ?: emptySet()

        return feedPostIds.map { it.toLong() }
    }

    /**
     * 개별 게시물 상세 조회
     */
    override fun getFeedPostFromHash(feedPostId: Long): FeedPostListDto? {
        val key = getHashKey(feedPostId)
        val json = redisTemplate.opsForValue().get(key)

        return json?.let {
            redisTemplate.expire(key, Duration.ofDays(7))
            objectMapper.readValue(json, FeedPostListDto::class.java)
        }
    }

    /**
     * 게시물 데이터를 Hash에 저장
     */
    override fun saveFeedPostDtoToHash(feedPostListDto: FeedPostListDto) {
        val key = getHashKey(feedPostListDto.feedPostId)
        val json = objectMapper.writeValueAsString(feedPostListDto)

        redisTemplate.opsForValue().set(key, json)
        redisTemplate.expire(key, Duration.ofDays(7))
    }

    /**
     * 좋아요 개수 업데이트
     */
    override fun updateLikeCount(feedPostId: Long, newLikeCount: Int) {
        val key = getHashKey(feedPostId)
        val json = redisTemplate.opsForValue().get(key) ?: return

        val feedPostListDto = objectMapper.readValue(json, FeedPostListDto::class.java)
        val updatedFeedPostDto = feedPostListDto.copy(likeCount = newLikeCount)

        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(updatedFeedPostDto))
    }

    /**
     * 피드 업데이트
     */
    override fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto) {
        val key = getHashKey(feedPostUpdateDto.feedPostId)
        val json = redisTemplate.opsForValue().get(key) ?: return

        val feedPostListDto = objectMapper.readValue(json, FeedPostListDto::class.java)
        val updatedFeedPostDto = feedPostListDto.copy(
            title = feedPostUpdateDto.title,
            content = feedPostUpdateDto.content
        )

        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(updatedFeedPostDto))
    }

    /**
     * 읽음 처리 (Set으로 저장, ZSet에서 제거)
     */
    override fun markReadAsFeed(userId: Long, readFeedPostIds: List<Long>) {
        val readKey = getReadMarkKey(userId)

        redisTemplate.opsForSet().add(readKey, *readFeedPostIds.map { it.toString() }.toTypedArray())
        redisTemplate.expire(readKey, Duration.ofDays(14))

        val key = getKey(userId)
        redisTemplate.opsForZSet().remove(key, *readFeedPostIds.map { it.toString() }.toTypedArray())
    }

    /**
     * 읽음 처리된 피드 목록 조회
     */
    override fun getReadMarkList(userId: Long): List<Long> {
        val readKey = getReadMarkKey(userId)

        return redisTemplate.opsForSet().members(readKey)?.map { it.toLong() } ?: emptyList()
    }

    /**
     * 키 만료 시간 설정
     */
    override fun setExpiration(key: String, duration: Duration) {
        redisTemplate.expire(key, duration)
    }

    private fun getKey(userId: Long) = "user:feed:$userId"
    private fun getHashKey(feedPostId: Long) = "feedpost:details:$feedPostId"
    private fun getReadMarkKey(userId: Long) = "read:feed:$userId"
}
