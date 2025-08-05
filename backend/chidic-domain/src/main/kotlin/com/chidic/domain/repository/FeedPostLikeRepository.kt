package com.chidic.domain.repository

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.FeedPostLike
import com.chidic.domain.entity.id.PostLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPostLikeRepository: JpaRepository<FeedPostLike, PostLikeId> {
    fun findByFeedPost(feedPost: FeedPost): List<FeedPostLike>
    fun countByFeedPost(feedPost: FeedPost): Long
}