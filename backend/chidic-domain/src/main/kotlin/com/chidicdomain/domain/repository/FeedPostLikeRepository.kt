package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.FeedPostLIke
import com.chidicdomain.domain.entity.id.PostLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPostLikeRepository: JpaRepository<FeedPostLIke, PostLikeId> {
    fun findByFeedPost(feedPost: FeedPost): List<FeedPostLIke>
}