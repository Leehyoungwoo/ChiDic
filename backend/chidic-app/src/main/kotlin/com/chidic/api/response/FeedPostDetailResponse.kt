package com.chidic.api.response

import com.chidic.dto.CommentDto
import java.time.LocalDateTime

data class FeedPostDetailResponse(
    val username: String,
    val title: String,
    val content: String,
    val comments: MutableList<CommentDto>,
    val likeCount: Int,
    val created: LocalDateTime
)