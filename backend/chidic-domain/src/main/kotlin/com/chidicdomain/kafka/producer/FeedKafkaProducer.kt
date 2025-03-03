package com.chidicdomain.kafka.producer

import com.chidicdomain.dto.FeedPostListDto
import com.chidicdomain.dto.FeedPostUpdateDto
import com.chidicdomain.kafka.event.FeedCreatedEvent
import com.chidicdomain.kafka.event.LikeEvent
import com.chidicdomain.kafka.event.UnlikeEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class FeedKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun sendFeedCreatedEvent(event: FeedCreatedEvent) {
        kafkaTemplate.send("feed-events", event)
    }

    fun sendFeedUpdatedEvent(event: FeedPostUpdateDto) {
        kafkaTemplate.send("feed-events", event)
    }

    fun sendFeedLikedEvent(event: LikeEvent) {
        kafkaTemplate.send("feed-like-events", event)
    }

    fun sendFeedUnlikedEvent(event: UnlikeEvent) {
        kafkaTemplate.send("feed-unlike-events", event)
    }

    fun sendFeedCacheUpdateEvent(event: FeedPostListDto) {
        kafkaTemplate.send("feed-update-events", event)
    }
}
