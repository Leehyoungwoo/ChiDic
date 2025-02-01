package com.chidicapp.api.commentlike

import com.chidicapp.security.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.commentlike.CommentLikeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comment/{commentId}/likes")
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

    @DeleteMapping
    fun unlikeComment(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable commentId: Long
    ) {
        commentLikeService.unlikeComment(userId, commentId);
    }
}