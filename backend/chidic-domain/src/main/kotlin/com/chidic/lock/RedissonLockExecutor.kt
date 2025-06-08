package com.chidic.lock

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedissonLockExecutor(
    private val redisson: RedissonClient
) : DistributedLockExecutor {
    override fun <T> execute(
        key: String, cache: () -> T?, critical: () -> T,
        waitMs: Long, leaseMs: Long, retryMs: Long
    ): T {
        val lock = redisson.getLock(key)
        val acquired = lock.tryLock(waitMs, leaseMs, TimeUnit.MILLISECONDS)

        return if (acquired) {
            try { cache() ?: critical() }
            finally { lock.unlock() }
        } else {
            cache() ?: if (lock.tryLock(retryMs, leaseMs, TimeUnit.MILLISECONDS)) {
                try { cache() ?: critical() } finally { lock.unlock() }
            } else throw IllegalStateException()
        }
    }
}