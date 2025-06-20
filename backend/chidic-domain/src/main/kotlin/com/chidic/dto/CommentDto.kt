package com.chidic.dto

import java.time.LocalDateTime

data class CommentDto (
    val userId: String,
    val content: String,
    val createdTime: LocalDateTime
)