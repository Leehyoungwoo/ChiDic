package com.chidicdomain.domain.service.comment

import com.chidicapp.api.request.CommentRequest
import com.chidicdomain.dto.CommentCreateDto

interface CommentService {
    fun createComment(commentCreateDto: CommentCreateDto)
    fun updateComment(commentId: Long, commentRequest: CommentRequest)
    fun deleteComment(commentId: Long)
}