package com.chidic.redis.service

import com.chidic.domain.entity.FeedPost
import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class RedisServiceImpl(
    private val redisTemplate: StringRedisTemplate,
    private val redissonClient: RedissonClient,
    private val objectMapper: ObjectMapper
) : RedisService {

    companion object {
        private const val MAX_FEED_SIZE = 600L
    }

    init {
        objectMapper.registerModule(JavaTimeModule())
    }

    /**
     * 뉴스 피드 저장
     */
    override fun saveFeedPostIds(userIds: List<Long>, feedPostId: Long) {
        userIds.forEach { userId ->
            this.pushFeedPostIdToEachUser(userId, feedPostId, System.currentTimeMillis().toDouble())
        }
    }

    private fun pushFeedPostIdToEachUser(userId: Long, feedPostId: Long, score: Double) {
        val redisKey = getKey(userId)

        // 1. PK만 ZSet에 추가
        redisTemplate.opsForZSet().add(redisKey, feedPostId.toString(), score)

        // 2. 사이즈 제한 (600개 유지)
        val currentSize = redisTemplate.opsForZSet().size(redisKey) ?: 0L
        if (currentSize > MAX_FEED_SIZE) {
            val removeCount = currentSize - MAX_FEED_SIZE
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
    override fun getFeedPostIdsForUser(userId: Long, start: Long, end: Long): List<Long> {
        val key = getKey(userId)

        return redisTemplate.opsForZSet()
            .reverseRange(key, start, end)
            ?.let { set -> set.map { it.toLong() } }
            ?: emptyList()
    }

    /**
     * 개별 게시물 상세 조회
     */
    override fun getFeedPostFromHash(feedPostId: Long): FeedPostListDto? {
        val key = getHashKey(feedPostId)

        val map = redisTemplate.opsForHash<String, String>().entries(key)
        if (map.isNullOrEmpty()) return null

        redisTemplate.expire(key, Duration.ofDays(7))

        return FeedPostListDto(
            feedPostId = feedPostId,
            title = map["title"] ?: "",
            content = map["content"] ?: "",
            writer = map["writer"] ?: "",
            writerProfile = map["writerProfile"],
            likeCount = map["likeCount"]?.toIntOrNull() ?: 0,
            commentCount = map["commentCount"]?.toLongOrNull() ?: 0L,
            createdAt = map["createdAt"]?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
        )
    }

    /**
     * 게시물 데이터를 Hash에 저장
     */
    override fun saveFeedPostDtoToHash(feedPostListDto: FeedPostListDto) {
        val key = getHashKey(feedPostListDto.feedPostId)
        val map = mapOf(
            "title" to feedPostListDto.title,
            "content" to feedPostListDto.content,
            "writer" to feedPostListDto.writer,
            "writerProfile" to (feedPostListDto.writerProfile ?: ""),
            "likeCount" to feedPostListDto.likeCount.toString(),
            "commentCount" to feedPostListDto.commentCount.toString(),
            "createdAt" to feedPostListDto.createdAt.toString()
        )

        redisTemplate.opsForHash<String, String>().putAll(key, map)
        redisTemplate.expire(key, Duration.ofDays(14))
    }

    /**
     * 좋아요 개수 업데이트
     */
    override fun updateLikeCount(feedPostId: Long) {
        val key = getHashKey(feedPostId)
        redisTemplate.opsForHash<String, String>().increment(key, "likeCount", 1)
    }

    /**
     *  좋아요 취소 개수 업데이트
     */
    override fun updateUnlikeCount(feedPostId: Long) {
        val key = getHashKey(feedPostId)
        redisTemplate.opsForHash<String, String>().increment(key, "likeCount", -1)
    }

    /**
     * 피드 업데이트
     */
    override fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto) {
        val key = getHashKey(feedPostUpdateDto.feedPostId)
        val ops = redisTemplate.opsForHash<String, String>()

        ops.put(key, "title", feedPostUpdateDto.title)
        ops.put(key, "content", feedPostUpdateDto.content)
    }

//    /**
//     * 읽음 처리 (Set으로 저장, ZSet에서 제거)
//     */
//    override fun markReadAsFeed(userId: Long, readFeedPostIds: List<Long>) {
//        val readKey = getReadMarkKey(userId)
//
//        redisTemplate.opsForSet().add(readKey, *readFeedPostIds.map { it.toString() }.toTypedArray())
//        redisTemplate.expire(readKey, Duration.ofDays(14))
//
//        val key = getKey(userId)
//        redisTemplate.opsForZSet().remove(key, *readFeedPostIds.map { it.toString() }.toTypedArray())
//    }

//    /**
//     * 읽음 처리된 피드 목록 조회
//     */
//    override fun getReadMarkList(userId: Long): List<Long> {
//        val readKey = getReadMarkKey(userId)
//
//        return redisTemplate.opsForSet().members(readKey)?.map { it.toLong() } ?: emptyList()
//    }

    override fun setIfNotExist(key: String, value: String, ttlInSeconds: Long): Boolean {
        // Redis에 key가 존재하지 않으면 값을 저장하고, TTL 설정
        val result = redisTemplate.opsForValue().setIfAbsent(key, value)
        if (result == true) {
            redisTemplate.expire(key, ttlInSeconds, TimeUnit.SECONDS)
        }
        return result ?: false
    }

    override fun deleteFeedPostHash(feedPostId: Long) {
        val key = getHashKey(feedPostId)
        redisTemplate.delete(key)
    }

    override fun publish(channel: String, feedPostId: Long) {
        val topic = redissonClient.getTopic(channel)
        topic.publish(feedPostId.toString())
    }

    override fun subscribeAndWait(
        channel: String,
        messageFilter: String,
        timeoutMs: Long
    ): String {
        val topic = redissonClient.getTopic(channel)
        val latch = java.util.concurrent.CountDownLatch(1)
        var messageReceived: String? = null

        // 1. 메시지 리스너 등록 (필터링 적용)
        val listenerId = topic.addListener(String::class.java) { _, msg ->
            if (msg == messageFilter) {
                messageReceived = msg
                latch.countDown() // 원하는 메시지면 대기 해제
            }
        }

        try {
            // 2. 타임아웃까지 대기
            latch.await(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)
        } finally {
            // 3. 리스너 해제 (메모리 누수 방지)
            topic.removeListener(listenerId)
        }

        // 메시지를 못 받으면 빈 문자열 반환
        return messageReceived ?: ""
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
