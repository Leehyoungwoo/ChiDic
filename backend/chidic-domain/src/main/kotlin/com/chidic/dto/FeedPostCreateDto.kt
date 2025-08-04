package com.chidic.dto

import jakarta.validation.constraints.Size

data class FeedPostCreateDto(
    val userId: Long,
    @field:Size(max = 50) val title: String,
    @field:Size(max = 1000) val content: String
)
