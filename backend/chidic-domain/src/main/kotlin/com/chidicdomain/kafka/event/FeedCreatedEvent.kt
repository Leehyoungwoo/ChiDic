package com.chidicdomain.kafka.event

import com.chidicdomain.dto.FeedPostListDto
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FeedCreatedEvent @JsonCreator constructor(
    @JsonProperty("userIds") val userIds: List<Long>,
    @JsonProperty("feedPostListDto") val feedPostListDto: FeedPostListDto
)
