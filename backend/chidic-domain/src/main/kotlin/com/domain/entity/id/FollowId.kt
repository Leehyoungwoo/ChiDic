package com.domain.entity.id

import java.io.Serializable
import java.util.UUID

data class FollowId (
    val followerId: UUID,
    val followingId: UUID
) : Serializable

