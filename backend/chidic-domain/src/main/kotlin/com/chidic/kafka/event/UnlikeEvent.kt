package com.chidic.kafka.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UnlikeEvent @JsonCreator constructor(
    @JsonProperty("userId")val userId: Long,
    @JsonProperty("feedPostId")val feedPostId: Long,
)
