package com.chidicdomain.dto

import java.time.LocalDateTime

data class FeedPostListDto(
    val feedPostId: Long,
    val title: String,
    val content: String,
    val writer: String,
    val writerProfile: String?,
    val likeCount: Long,
    val commentCont: Long,
    val createdAt: LocalDateTime
)
