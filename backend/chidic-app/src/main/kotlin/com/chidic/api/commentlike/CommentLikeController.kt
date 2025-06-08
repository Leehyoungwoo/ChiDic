package com.chidic.api.commentlike

import com.chidic.security.auth.model.OAuth2UserDetails
import com.chidic.domain.service.commentlike.CommentLikeService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comment/{commentId}/likes")
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeComment(
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @PathVariable commentId: Long
    ) {
        val userId = principal.getId()
        commentLikeService.likeComment(userId, commentId);
    }

    @DeleteMapping
    fun unlikeComment(
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @PathVariable commentId: Long
    ) {
        val userId = principal.getId()
        commentLikeService.unlikeComment(userId, commentId);
    }
}