package com.chidicapp.api.comment

import com.chidiccommon.dto.CommentRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.comment.CommentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

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
        @RequestBody commentRequest: CommentRequest
    ) {
        commentService.createComment(feedPostId, userId, commentRequest)
    }

    // 아무나 댓글 삭제할 수 없게 인가 처리 필요
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateComment(
        @PathVariable commentId: Long,
        @GetUserIdFromPrincipal userId: Long,
        @RequestBody commentRequest: CommentRequest
    ) {
        commentService.updateComment(commentId, commentRequest)
    }

    // 작성인만 삭제할 수 있게 인가처리 필요
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteComment(
        @PathVariable commentId: Long,
        @GetUserIdFromPrincipal userId: Long
    ) {
        commentService.deleteComment(commentId)
    }
}