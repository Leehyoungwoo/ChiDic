package com.chidicdomain.domain.mapper.feedpost

import com.chidiccommon.dto.CommentDto
import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
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

    override fun toFeedPostDetailResponse(feedPost: FeedPost): FeedPostDetailResponse {
        return FeedPostDetailResponse(
            title = feedPost.title,
            content = feedPost.content,
            created = feedPost.createdAt,
            comments = feedPost.comments.map {
                CommentDto(
                    userId = it.user.username,
                    content = it.content,
                    createdTime = it.createdAt
                )
            }.toMutableList()
        )
    }
}