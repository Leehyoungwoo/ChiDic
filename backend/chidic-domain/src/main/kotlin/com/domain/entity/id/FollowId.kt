package com.domain.entity.id

import java.io.Serializable
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class FollowId (
    @Column(name = "follower_id")
    val followerId: UUID,

    @Column(name = "following_id")
    val followeeId: UUID
) : Serializable
