package com.chidicapp.api.commentlike

import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.commentlike.CommentLikeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController("/api/comment/{commentId}/likes")
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeComment(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable commentId: Long
    ) {
        commentLikeService.likeComment(userId, commentId);
    }

    fun unlikeComment(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable commentId: Long
    ) {
        commentLikeService.unlikeComment(userId, commentId);
    }
}