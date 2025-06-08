package com.chidic.dto

data class CommentCreateDto (
    val userId: Long,
    val feedPostId: Long,
    val content: String
)