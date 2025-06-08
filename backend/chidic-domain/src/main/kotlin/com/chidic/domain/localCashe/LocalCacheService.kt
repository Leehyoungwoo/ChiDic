package com.chidic.domain.localCashe

interface LocalCacheService {
    fun putCache(key: String, value: Any)
    fun getCache(key: String): Any?
    fun removeCache(key: String)
}