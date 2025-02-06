package com.chidicdomain.kafka.consumer

import com.chidicdomain.dto.FeedPostUpdateDto
import com.chidicdomain.kafka.event.FeedCreatedEvent
import com.chidicdomain.kafka.event.LikeEvent
import com.chidicdomain.kafka.event.UnlikeEvent
import com.chidicdomain.redis.service.RedisService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class FeedKafkaConsumer(
    private val redisService: RedisService
) {

    @KafkaListener(topics = ["feed-events"], groupId = "feed-group")
    fun consumeFeedCreatedEvent(event: FeedCreatedEvent) {
        event.userIds.parallelStream().forEach { userId ->
            redisService.saveFeedPost(userId, event.feedPostListDto)
        }
    }

    @KafkaListener(topics = ["feed-like-events"], groupId = "feed-group")
    fun consumeLikeEvent(event: LikeEvent) {
        redisService.updateLikeCount(event.feedPostId, event.likeCount)
    }

    @KafkaListener(topics = ["feed-unlike-events"], groupId = "feed-group")
    fun consumeUnlikeEvent(event: UnlikeEvent) {
        redisService.updateLikeCount(event.feedPostId, event.likeCount)
    }

    @KafkaListener(topics = ["feed-update-events"], groupId = "feed-group")
    fun consumeFeedUpdateEvent(event: FeedPostUpdateDto) {
        redisService.updateFeed(event)
    }
}
