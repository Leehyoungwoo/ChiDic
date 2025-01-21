package com.chidicdomain.domain.entity.id

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CommentLikeId (
    val userId: Long,
    val commentId: Long
): Serializable