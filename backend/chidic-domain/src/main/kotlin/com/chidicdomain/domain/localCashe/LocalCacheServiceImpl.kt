package com.chidicdomain.domain.localCashe

import com.google.common.cache.CacheBuilder
import com.google.common.cache.Cache
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class LocalCacheServiceImpl: LocalCacheService {

    private val cache: Cache<String, Any> = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.HOURS)
        .maximumSize(10000)
        .build()

    override fun putCache(key: String, value: Any) {
        cache.put(key, value)
    }

    override fun getCache(key: String): Any? {
        return cache.getIfPresent(key)
    }

    override fun removeCache(key: String) {
        cache.invalidate(key)
    }
}