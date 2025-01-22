package com.chidicdomain.domain.entity

import com.chidicdomain.domain.entity.id.FollowId
import jakarta.persistence.*

@Entity
class Follow(
    @EmbeddedId
    val id: FollowId,

    @Column(name = "is_followed", nullable = false)
    var isFollowed: Boolean = false
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    var follower: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", insertable = false, updatable = false)
    var followee: User? = null

    fun follow() {
        isFollowed = true
    }
}
