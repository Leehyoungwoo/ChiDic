package com.domain.entity

import com.domain.entity.id.PostLikeId
import jakarta.persistence.*

@Entity
class PostLike(
    @EmbeddedId
    var id: PostLikeId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: FeedPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User
)