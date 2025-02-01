package com.chidicdomain.dto

import java.time.LocalDateTime

data class FeedPostDetailDto(
    val title: String,
    val content: String,
    val comments: MutableList<CommentDto>,
    val created: LocalDateTime
)
