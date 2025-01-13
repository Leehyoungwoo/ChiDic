package com.chidicdomain.domain.entity.id

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class FollowId (
    @Column(name = "follower_id")
    val followerId: Long,

    @Column(name = "following_id")
    val followeeId: Long
) : Serializable
