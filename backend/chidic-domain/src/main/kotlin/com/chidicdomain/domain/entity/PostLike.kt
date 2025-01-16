package com.chidicdomain.domain.entity

import com.chidicdomain.domain.entity.id.PostLikeId
import jakarta.persistence.*

@Entity
class PostLike(
    @EmbeddedId
    var id: PostLikeId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    var post: FeedPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    var user: User
)
