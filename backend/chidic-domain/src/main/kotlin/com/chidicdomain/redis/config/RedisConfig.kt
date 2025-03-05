import io.lettuce.core.ClientOptions
import io.lettuce.core.SocketOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisNode
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisClusterConfig {

    @Value("\${spring.data.redis.cluster.nodes}")
    lateinit var clusterNodes: List<String>

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisNodes = clusterNodes.map { clusterNode ->
            val (host, port) = clusterNode.split(":")
            RedisNode(host, port.toInt())
        }

        val clusterConfiguration = RedisClusterConfiguration()
        clusterConfiguration.setClusterNodes(redisNodes)

        // Socket 옵션
        val socketOptions = SocketOptions.builder()
            .connectTimeout(Duration.ofSeconds(5))  // 타임아웃을 5초로 설정
            .keepAlive(true)
            .build()

        // Cluster topology refresh 옵션
        val clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
            .dynamicRefreshSources(true)
            .enableAllAdaptiveRefreshTriggers()
            .enablePeriodicRefresh(Duration.ofSeconds(30))  // 30초 주기로 갱신
            .build()

        // Cluster Client 옵션
        val clientOptions = ClusterClientOptions.builder()
            .autoReconnect(true)
            .topologyRefreshOptions(clusterTopologyRefreshOptions)
            .socketOptions(socketOptions)
            .build()

        // Lettuce Client 옵션에 autoReconnect 설정 추가
        val clientConfiguration = LettuceClientConfiguration.builder()
            .clientOptions(ClientOptions.builder().autoReconnect(true).build())
            .commandTimeout(Duration.ofMillis(3000L))
            .build()

        return LettuceConnectionFactory(clusterConfiguration, clientConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = StringRedisSerializer()
        redisTemplate.connectionFactory = redisConnectionFactory()
        return redisTemplate
    }
}
