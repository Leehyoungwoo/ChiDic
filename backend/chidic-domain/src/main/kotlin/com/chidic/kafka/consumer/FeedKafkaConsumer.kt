package com.chidic.kafka.consumer

import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreateEvent
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import com.chidic.redis.service.RedisService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
@EnableKafka
class FeedKafkaConsumer(
    private val redisService: RedisService,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(topics = ["feed-create-events"], groupId = "feed-group")
    fun consumeFeedCreatedEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedCreateEvent::class.java)

            // dto 먼저 캐싱
            redisService.saveFeedPostDtoToHash(event.feedPostListDto)

            // fan-out-write
            event.userIds.parallelStream().forEach { userId ->
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
            event.let {
                redisService.updateLikeCount(it.feedPostId)
            }
        } catch (e: Exception) {
            println("Error processing like event: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-unlike-events"], groupId = "feed-group")
    fun consumeUnlikeEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, UnlikeEvent::class.java)
            event.let {
                redisService.updateLikeCount(it.feedPostId)
            }
        } catch (e: Exception) {
            println("Error processing unlike event: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-update-events"], groupId = "feed-group")
    fun consumeUpdateEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedPostUpdateDto::class.java)
            event.let {
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
            event.let {
                redisService.saveFeedPostDtoToHash(it)
            }
        } catch (e: Exception) {
            println("Error processing unlike event: ${e.message}")
        }
    }

    private fun <T> convertMessageToEvent(message: Message<String>, eventType: Class<T>): T {
        val jsonString = message.payload
        return try {
            objectMapper.readValue(jsonString, eventType)
        } catch (e: Exception) {
            println("Error converting message: ${e.message}")
            throw e
        }
    }
}
