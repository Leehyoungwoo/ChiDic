package com.chidicdomain.domain.mapper.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import org.springframework.stereotype.Component

@Component
class FeedPostMapperImpl: FeedPostMapper {
    override fun toEntity(user: User, feedPostCreateRequest: FeedPostCreateRequest): FeedPost {
        return FeedPost(
            user = user,
            title = feedPostCreateRequest.title,
            content = feedPostCreateRequest.content
        )
    }
}