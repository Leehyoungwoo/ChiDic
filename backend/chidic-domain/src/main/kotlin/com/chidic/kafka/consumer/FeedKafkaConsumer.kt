package com.chidic.kafka.consumer

import com.chidic.domain.repository.FeedPostRepository
import com.chidic.domain.service.feedpostlike.FeedPostLikeService
import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreateEvent
import com.chidic.kafka.event.FeedFanoutEvent
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import com.chidic.kafka.producer.FeedKafkaProducer
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
    private val feedPostLikeService: FeedPostLikeService,
    private val objectMapper: ObjectMapper,
    private val feedKafkaProducer: FeedKafkaProducer
) {

    @KafkaListener(topics = ["feed-create-events"], groupId = "feed-group")
    fun consumeFeedCreatedEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, FeedCreateEvent::class.java)

            // dto 먼저 캐싱
            redisService.saveFeedPostDtoToHash(event.feedPostListDto)

            // fan-out-write 이벤트 발행
            event.userIds.chunked(1000).forEach { batch ->
                val fanoutEvent = FeedFanoutEvent(
                    feedPostId = event.feedPostListDto.feedPostId,
                    followerIds = batch
                )
                feedKafkaProducer.sendFeedFanoutEvents(fanoutEvent)
            }
        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
        }
    }

    @KafkaListener(topics = ["feed-fanout-events"], groupId = "feed-group")
    fun consumeFanoutEvent(message: Message<String>) {
        val event = convertMessageToEvent(message, FeedFanoutEvent::class.java)
        redisService.saveFeedPostIds(event.followerIds, event.feedPostId)
    }

    @KafkaListener(topics = ["feed-like-events"], groupId = "feed-group")
    fun consumeLikeEvent(message: Message<String>) {
        try {
            val event = convertMessageToEvent(message, LikeEvent::class.java)
            event.let {
                val idempotencyKey = "like:${it.feedPostId}:${it.userId}"
                val acquired = redisService.setIfNotExist(idempotencyKey, "1", 5)

                // 멱등성 보장
                if (!acquired) return

                val dbUpdated = feedPostLikeService.createLikeAndIncrementCount(it.userId, it.feedPostId)

                if (dbUpdated) {
                    redisService.updateUnlikeCount(it.feedPostId)
                }
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
                val idempotencyKey = "unlike:${it.feedPostId}:${it.userId}"
                val acquired = redisService.setIfNotExist(idempotencyKey, "1", 5)

                // 멱등성 보장
                if (!acquired) return

                val dbUpdate = feedPostLikeService.decrementCount(it.userId, it.feedPostId)

                if (dbUpdate) {
                    redisService.updateLikeCount(it.feedPostId)
                }
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
