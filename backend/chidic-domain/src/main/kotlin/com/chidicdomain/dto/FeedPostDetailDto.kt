package com.chidicdomain.dto

import com.chidiccommon.dto.CommentDto
import java.time.LocalDateTime

data class FeedPostDetailDto(
    val title: String,
    val content: String,
    val comments: MutableList<CommentDto>,
    val created: LocalDateTime
)
