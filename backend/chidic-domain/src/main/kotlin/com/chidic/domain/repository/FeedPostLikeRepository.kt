package com.chidic.domain.repository

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.FeedPostLIke
import com.chidic.domain.entity.id.PostLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPostLikeRepository: JpaRepository<FeedPostLIke, PostLikeId> {
    fun findByFeedPost(feedPost: FeedPost): List<FeedPostLIke>
    fun countByFeedPost(feedPost: FeedPost): Long
}