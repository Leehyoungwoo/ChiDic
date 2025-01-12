package com.domain.entity

import com.domain.entity.id.FollowId
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Follow (
    @EmbeddedId
    val id: FollowId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false, insertable = false, updatable = false)
    var follower: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", nullable = false, insertable = false, updatable = false)
    var following: User,
)
