package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FeedPostRepository : JpaRepository<FeedPost, Long>{
    @Query("SELECT fp FROM FeedPost fp " +
            "JOIN FETCH fp.user " +
            "LEFT JOIN FETCH fp.comments c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE fp.id = :id")
    fun findFeedPostWithUserAndComments(@Param("id") id: Long): FeedPost?

    fun findByUserIn(userList: List<User>, pageable: Pageable): List<FeedPost>

    fun findByUserInAndIdLessThan(userList: List<User>, lastFeedPostId: Long, pageable: Pageable): List<FeedPost>

    @Query("""
    SELECT f FROM FeedPost f 
    LEFT JOIN FETCH f.user 
    LEFT JOIN FETCH f.comments 
    WHERE f.id IN :ids 
    ORDER BY f.id DESC
""")
    fun findAllByIdIn(@Param("ids") ids: List<Long>): List<FeedPost>
}