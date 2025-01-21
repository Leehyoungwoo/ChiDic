package com.chidicdomain.domain.entity.id

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CommentLikeId (
    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "comment_id")
    val commentId: Long
): Serializable