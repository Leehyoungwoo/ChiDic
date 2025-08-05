package com.chidic.domain.entity

import com.chidic.domain.entity.id.PostLikeId
import jakarta.persistence.*

@Entity
class FeedPostLike(
    @EmbeddedId
    val id: PostLikeId,

    @Column(name = "is_liked", nullable = false)
    var isLiked: Boolean = false
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    var feedPost: FeedPost? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null

    fun associateFeedAndUser(feedPost: FeedPost, user: User) {
        this.feedPost = feedPost
        this.user = user
    }

    fun likePost() {
        isLiked = true
    }

    fun unlikePost() {
        isLiked = false
    }
}