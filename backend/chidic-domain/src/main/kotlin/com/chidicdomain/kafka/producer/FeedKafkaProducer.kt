package com.chidicdomain.kafka.producer

import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto
import com.chidicdomain.kafka.event.FeedCreatedEvent
import com.chidicdomain.kafka.event.LikeEvent
import com.chidicdomain.kafka.event.UnlikeEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class FeedKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {
    fun sendFeedCreatedEvent(event: FeedCreatedEvent) {
        sendMessage("feed-events", event)
    }

    fun sendFeedUpdatedEvent(event: FeedPostUpdateDto) {
        sendMessage("feed-update-events", event)
    }

    fun sendFeedLikedEvent(event: LikeEvent) {
        sendMessage("feed-like-events", event)
    }

    fun sendFeedUnlikedEvent(event: UnlikeEvent) {
        sendMessage("feed-unlike-events", event)
    }

    fun sendFeedCacheUpdateEvent(event: FeedPostListDto) {
        sendMessage("feed-update-events", event)
    }

    private fun sendMessage(topic: String, event: Any) {
        val jsonString = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(topic, jsonString)
    }
}
