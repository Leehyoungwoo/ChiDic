package com.chidicapp.api.response

import com.chidicdomain.dto.CommentDto
import java.time.LocalDateTime

data class FeedPostDetailResponse(
    val title: String,
    val content: String,
    val comments: MutableList<CommentDto>,
    val created: LocalDateTime
)