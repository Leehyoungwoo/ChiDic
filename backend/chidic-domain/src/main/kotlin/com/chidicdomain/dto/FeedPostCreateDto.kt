package com.chidicdomain.dto

data class FeedPostCreateDto(
    val userId: Long,
    val title: String,
    val content: String
)
