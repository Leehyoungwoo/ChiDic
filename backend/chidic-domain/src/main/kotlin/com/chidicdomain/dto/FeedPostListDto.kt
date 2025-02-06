package com.chidicdomain.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class FeedPostListDto @JsonCreator constructor(
    @JsonProperty("feedPostId") val feedPostId: Long,
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("writer") val writer: String,
    @JsonProperty("writerProfile") val writerProfile: String?,
    @JsonProperty("likeCount") val likeCount: Int,
    @JsonProperty("commentCount") val commentCount: Long,
    @JsonProperty("createdAt") val createdAt: LocalDateTime
)
