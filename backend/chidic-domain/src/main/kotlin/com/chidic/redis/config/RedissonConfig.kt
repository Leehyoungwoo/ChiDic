package com.chidic.redis.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class RedissonConfig {

    @Value("\${spring.data.redis.cluster.nodes}")
    private lateinit var clusterNodes: List<String>


    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config()

        // ⇣ 클러스터 모드
        val clusterServers = config.useClusterServers()
            .setScanInterval(5_000)
            .setRetryAttempts(3)
            .setRetryInterval(1_500)

        clusterNodes.forEach { node ->
            clusterServers.addNodeAddress("redis://$node")
        }

        config.threads = Runtime.getRuntime().availableProcessors() * 2
        config.nettyThreads = config.threads

        return Redisson.create(config)
    }
}