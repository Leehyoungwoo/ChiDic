package com.chidicdomain.domain.mapper.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User

interface FeedPostMapper {
    fun toEntity(user: User, feedPostCreateRequest: FeedPostCreateRequest): FeedPost
}