package com.chidicdomain.domain.mapper.comment

import com.chidiccommon.dto.CommentCreateRequest
import com.chidicdomain.domain.entity.Comment
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import org.springframework.stereotype.Component

@Component
class CommentMapperImpl: CommentMapper {
    override fun toEntity(user: User, feedPost: FeedPost, commentCreateRequest: CommentCreateRequest): Comment {
        return Comment(
            user = user,
            feedPost = feedPost,
            content = commentCreateRequest.content
        )
    }
}