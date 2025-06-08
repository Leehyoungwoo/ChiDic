package com.chidic.kafka.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LikeEvent @JsonCreator constructor(
    @JsonProperty("feedPostId")val feedPostId: Long,
    @JsonProperty("likeCount")val likeCount: Int
)