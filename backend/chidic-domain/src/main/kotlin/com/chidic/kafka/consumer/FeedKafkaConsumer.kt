package com.chidic.kafka.consumer

import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreatedEvent
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import com.chidic.redis.service.RedisService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.messaging.converter.MessageConversionException
import org.springframework.stereotype.Service

@Service
@EnableKafka
class FeedKafkaConsumer(
    private val redisService: RedisService,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(topics = ["feed-events"], groupId = "feed-group")
    fun consumeFeedCreatedEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedCreatedEvent::class.java)
            event?.userIds?.parallelStream()?.forEach { userId ->
                redisService.saveFeedPost(userId, event.feedPostListDto)
            }
        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-like-events"], groupId = "feed-group")
    fun consumeLikeEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, LikeEvent::class.java)
            event?.let {
                redisService.updateLikeCount(it.feedPostId, it.likeCount)
            }
        } catch (e: Exception) {
            println("Error processing like event: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-unlike-events"], groupId = "feed-group")
    fun consumeUnlikeEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, UnlikeEvent::class.java)
            event?.let {
                redisService.updateLikeCount(it.feedPostId, it.likeCount)
            }
        } catch (e: Exception) {
            println("Error processing unlike event: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-update-events"], groupId = "feed-group")
    fun consumeUpdateEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedPostUpdateDto::class.java)
            event?.let {
                redisService.updateFeed(event)
            }
        } catch (e: Exception) {
            println("Error processing unlike event: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-cache-update-events"], groupId = "feed-group")
    fun consumeCacheUpdateEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedPostListDto::class.java)
            event?.let {
                // 고유 식별자 생성
                val messageId = createCacheUpdateMessageId(it.feedPostId, it.likeCount)

                // Redis에서 메시지가 이미 처리되었는지 확인
                val alreadyProcessed = redisService.setIfNotExist(messageId, "processed", 60 * 1) // 5분 동안 중복 방지


                if (alreadyProcessed) {
                    // 캐시 갱신 작업 수행
                    redisService.saveFeedPostDtoToHash(it)
                } else {
                    println("중복된 메시지입니다: $messageId")
                }
            }
        } catch (e: Exception) {
            println("Error processing unlike event: ${e.message}")
        }
    }

    private fun <T> convertMessageToEvent(message: Message<String>, eventType: Class<T>): T? {
        return try {
            val jsonString = message.payload
            objectMapper.readValue(jsonString, eventType)
        } catch (e: MessageConversionException) {
            println("Error converting message: ${e.message}")
            null
        }
    }

    private fun createCacheUpdateMessageId(feedPostId: Long, likeCount: Int): String {
        return "feedPost:$feedPostId:likeCount:$likeCount"
    }
}
