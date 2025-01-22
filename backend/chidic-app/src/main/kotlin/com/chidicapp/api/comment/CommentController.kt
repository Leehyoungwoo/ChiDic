package com.chidicapp.api.comment

import com.chidiccommon.dto.CommentCreateRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.comment.CommentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/{feedPostId}/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createComment(
        @PathVariable feedPostId: Long,
        @GetUserIdFromPrincipal userId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest
    ) {
        commentService.createComment(feedPostId, userId, commentCreateRequest)
    }
}