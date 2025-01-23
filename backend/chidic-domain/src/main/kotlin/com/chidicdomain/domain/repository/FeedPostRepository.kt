package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPostRepository : JpaRepository<FeedPost, Long>{
    fun findByUserIn(userList: List<User>, pageable: Pageable): Page<FeedPost>
}