package com.chidic.domain.repository

import com.chidic.domain.entity.FeedPostLIke
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LockRepository : JpaRepository<FeedPostLIke, String> {

    @Query(value = "SELECT GET_LOCK(:key, 3000)", nativeQuery = true)
    fun getLock(key: String): Int

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    fun releaseLock(key: String): Int
}
