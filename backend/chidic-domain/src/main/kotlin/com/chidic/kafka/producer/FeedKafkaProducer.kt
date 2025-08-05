package com.chidic.kafka.producer

import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreateEvent
import com.chidic.kafka.event.FeedFanoutEvent
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class FeedKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {
    fun sendFeedCreatedEvent(event: FeedCreateEvent) {
        sendMessage("feed-create-events", event)
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
        sendMessage("feed-cache-update-events", event)
    }

    fun sendFeedFanoutEvents(event: FeedFanoutEvent) {
        sendMessage("feed-fanout-events", event)
    }

    private fun sendMessage(topic: String, event: Any) {
        val jsonString = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(topic, jsonString)
    }
}
