package com.domain.entity.id

import java.io.Serializable
import java.util.UUID

data class PostLikeId (
    var postId: Long,
    var userId: UUID
) : Serializable