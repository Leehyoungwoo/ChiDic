package com.chidic.domain.repository

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FeedPostRepository : JpaRepository<FeedPost, Long> {

    @Query(
        """ 
        SELECT fp FROM FeedPost fp 
        JOIN FETCH fp.user 
        LEFT JOIN FETCH fp . comments c 
        LEFT JOIN FETCH c . user 
        WHERE fp . id = :id
        """
    )
    fun findFeedPostWithUserAndComments(
        @Param("id") id: Long)
    : FeedPost?

    @Query(
        """
        SELECT f FROM FeedPost f
        JOIN FETCH f.user
        WHERE f.user IN :users
          AND f.id < :lastId
        """
    )
    fun findLatestPosts(
        @Param("users") users: List<User>,
        @Param("lastId") lastId: Long,
        pageable: Pageable
    ): List<FeedPost>



//    @Query("SELECT p FROM FeedPost p WHERE p.user IN :users AND p.id NOT IN :readPostIds ORDER BY p.id DESC")
//    fun findUnreadFeedPosts(users: List<User>, readPostIds: List<Long>, pageable: Pageable): List<FeedPost>

    fun findByUserIn(userList: List<User>, pageable: Pageable): List<FeedPost>

    fun findByUserInAndIdLessThan(userList: List<User>, lastFeedPostId: Long, pageable: Pageable): List<FeedPost>

    @Query(
        """
                SELECT f FROM FeedPost f
                LEFT JOIN FETCH f . user
                LEFT JOIN FETCH f . comments
                WHERE f . id IN :ids
        ORDER BY f.id DESC
                """
    )
    fun findAllByIdIn(@Param("ids") ids: List<Long>): List<FeedPost>
}