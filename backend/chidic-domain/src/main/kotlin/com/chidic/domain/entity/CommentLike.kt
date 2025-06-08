package com.chidic.domain.entity

import com.chidic.domain.entity.id.CommentLikeId
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class CommentLike(
    @EmbeddedId
    val id: CommentLikeId,

    @Column(name = "is_liked", nullable = false)
    var isLiked: Boolean = false
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    var comment: Comment? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null

    fun associateCommentAndUser(comment: Comment, user: User) {
        this.comment = comment
        this.user = user
    }

    fun likeComment() {
        isLiked = true
    }

    fun UnlikeComment() {
        isLiked = false
    }
}