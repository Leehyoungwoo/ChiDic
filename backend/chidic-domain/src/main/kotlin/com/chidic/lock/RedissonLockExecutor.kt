package com.chidic.lock

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedissonLockExecutor(
    private val redisson: RedissonClient
) : DistributedLockExecutor {

    override fun <T> execute(
        key: String,
        cache: () -> T?,
        critical: () -> T,
        onLockFail: (() -> T?)?,
        waitMs: Long,
        leaseMs: Long,
        retryMs: Long
    ): T {
        val lock = redisson.getLock(key)
        var acquired = false

        try {
            // 2. 분산 락 시도
            acquired = lock.tryLock(waitMs, leaseMs, TimeUnit.MILLISECONDS)

            if (acquired) {
                // 락 성공 → critical 실행
                val result = critical()
                return result
            } else {
                // 락 실패 → fallback
                onLockFail?.let { return it() ?: throw IllegalStateException("Lock fail but no data") }
                    ?: throw IllegalStateException("Lock fail and no fallback provided")
            }
        } finally {
            // 3. 락 해제
            if (acquired && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}
