package com.chidic.domain.service.comment

import com.chidic.dto.CommentCreateDto
import com.chidic.dto.CommentUpdateDto

interface CommentService {
    fun createComment(commentCreateDto: CommentCreateDto)
    fun updateComment(commentUpdateDto: CommentUpdateDto)
    fun deleteComment(commentId: Long)
}

class CommentNotFoundException(message: String) : RuntimeException(message)