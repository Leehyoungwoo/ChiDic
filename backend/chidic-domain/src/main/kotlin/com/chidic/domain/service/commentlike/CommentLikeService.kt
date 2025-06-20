package com.chidic.domain.service.commentlike

interface CommentLikeService {
    fun likeComment(userId: Long, commentId: Long)
    fun unlikeComment(userId: Long, commentId: Long)
}