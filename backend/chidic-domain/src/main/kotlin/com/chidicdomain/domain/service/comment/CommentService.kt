package com.chidicdomain.domain.service.comment

import com.chidicdomain.dto.CommentCreateDto
import com.chidicdomain.dto.CommentUpdateDto

interface CommentService {
    fun createComment(commentCreateDto: CommentCreateDto)
    fun updateComment(commentUpdateDto: CommentUpdateDto)
    fun deleteComment(commentId: Long)
}