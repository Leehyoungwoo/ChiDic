package com.chidicdomain.domain.service.comment

import com.chidiccommon.dto.CommentCreateRequest

interface CommentService {
    fun createComment(feedPostId: Long, userId: Long, commentCreateRequest: CommentCreateRequest)
}