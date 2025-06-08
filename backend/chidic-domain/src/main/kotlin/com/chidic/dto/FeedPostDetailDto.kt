package com.chidic.dto

import java.time.LocalDateTime

data class FeedPostDetailDto(
    val username: String,
    val title: String,
    val content: String,
    val comments: MutableList<CommentDto>,
    val likeCount: Int,
    val created: LocalDateTime
)
