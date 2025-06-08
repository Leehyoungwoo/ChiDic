package com.chidic.dto

data class FeedPostCreateDto(
    val userId: Long,
    val title: String,
    val content: String
)
