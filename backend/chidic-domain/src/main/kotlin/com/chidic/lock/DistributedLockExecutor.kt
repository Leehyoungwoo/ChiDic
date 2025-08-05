package com.chidic.lock

interface DistributedLockExecutor {
    fun <T> execute(
        key: String,
        cache: () -> T?,
        critical: () -> T,
        onLockFail: (() -> T?)? = null,
        waitMs: Long = 300,
        leaseMs: Long = 3_000,
        retryMs: Long = 50
    ): T
}