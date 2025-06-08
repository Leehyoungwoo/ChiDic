package com.chidic.kafka.config

import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreatedEvent
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConfig {

    @Bean
    fun feedEventsTopic(): NewTopic {
        return TopicBuilder.name("feed-events").partitions(3).replicas(1).build()
    }

    @Bean
    fun feedLikeEventsTopic(): NewTopic {
        return TopicBuilder.name("feed-like-events").partitions(3).replicas(1).build()
    }

    @Bean
    fun feedUnlikeEventsTopic(): NewTopic {
        return TopicBuilder.name("feed-unlike-events").partitions(3).replicas(1).build()
    }

    @Bean
    fun feedUpdateEventsTopic(): NewTopic {
        return TopicBuilder.name("feed-update-events").partitions(3).replicas(1).build()
    }

    @Bean
    fun eventTopics(): Map<String, Class<out Any>> {
        return mapOf(
            "feed-events" to FeedCreatedEvent::class.java,
            "feed-like-events" to LikeEvent::class.java,
            "feed-unlike-events" to UnlikeEvent::class.java,
            "feed-update-events" to FeedPostUpdateDto::class.java
        )
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val jsonDeserializer = JsonDeserializer(Any::class.java)
        jsonDeserializer.addTrustedPackages("com.chidicdomain.kafka.event")
        jsonDeserializer.setUseTypeHeaders(false)

        return DefaultKafkaConsumerFactory(
            mapOf(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"),
            StringDeserializer(),
            ErrorHandlingDeserializer(jsonDeserializer)
        )
    }
}
