package com.chidicdomain.domain.service.comment

import com.chidiccommon.dto.CommentRequest

interface CommentService {
    fun createComment(feedPostId: Long, userId: Long, commentRequest: CommentRequest)
    fun updateComment(commentId: Long, commentRequest: CommentRequest)
}