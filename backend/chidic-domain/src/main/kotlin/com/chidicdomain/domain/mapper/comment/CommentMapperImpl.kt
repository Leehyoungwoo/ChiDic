package com.chidicdomain.domain.mapper.comment

import com.chidiccommon.dto.CommentRequest
import com.chidicdomain.domain.entity.Comment
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import org.springframework.stereotype.Component

@Component
class CommentMapperImpl: CommentMapper {
    override fun toEntity(user: User, feedPost: FeedPost, commentRequest: CommentRequest): Comment {
        return Comment(
            user = user,
            feedPost = feedPost,
            content = commentRequest.content
        )
    }
}