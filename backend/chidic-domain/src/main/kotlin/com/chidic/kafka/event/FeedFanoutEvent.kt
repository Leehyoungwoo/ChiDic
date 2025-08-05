package com.chidic.kafka.event

import com.fasterxml.jackson.annotation.JsonProperty

data class FeedFanoutEvent (
    @JsonProperty("feedPostId") val feedPostId: Long,
    @JsonProperty("followerIds") val followerIds: List<Long>
)