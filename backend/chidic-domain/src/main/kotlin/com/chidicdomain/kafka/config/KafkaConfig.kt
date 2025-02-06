package com.chidicdomain.kafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

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
}
