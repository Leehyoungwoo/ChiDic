package com.chidicdomain.dto

import java.time.LocalDateTime

data class FeedPostListDto(
    val feedPostId: Long,
    val title: String,
    val content: String,
    val writer: String,
    val writerProfile: String?,
    val likeCount: Int,
    val commentCount: Long,
    val createdAt: LocalDateTime
)
