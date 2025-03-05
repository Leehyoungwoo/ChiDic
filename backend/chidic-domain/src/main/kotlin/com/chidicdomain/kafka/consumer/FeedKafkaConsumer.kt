package com.chidicdomain.kafka.consumer

import com.chidicdomain.kafka.event.FeedCreatedEvent
import com.chidicdomain.kafka.event.LikeEvent
import com.chidicdomain.kafka.event.UnlikeEvent
import com.chidicdomain.redis.service.RedisService
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

    private fun <T> convertMessageToEvent(message: Message<String>, eventType: Class<T>): T? {
        return try {
            val jsonString = message.payload
            objectMapper.readValue(jsonString, eventType)
        } catch (e: MessageConversionException) {
            println("Error converting message: ${e.message}")
            null
        }
    }
}
