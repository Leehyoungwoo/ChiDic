package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.FeedPost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPostRepository : JpaRepository<FeedPost, Long>{
}