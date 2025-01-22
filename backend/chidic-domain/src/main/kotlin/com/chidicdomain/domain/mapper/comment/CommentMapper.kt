package com.chidicdomain.domain.mapper.comment

import com.chidiccommon.dto.CommentRequest
import com.chidicdomain.domain.entity.Comment
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User

interface CommentMapper {
    fun toEntity(user: User, feedPost: FeedPost, commentRequest: CommentRequest): Comment
}