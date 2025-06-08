package com.chidic.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FeedPostUpdateDto @JsonCreator constructor(
    @JsonProperty("feedPostId")val feedPostId: Long,
    @JsonProperty("title")val title: String,
    @JsonProperty("content")val content: String
)