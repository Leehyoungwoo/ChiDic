package com.chidicapp.api.feedpostlike

import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.feedpostlike.FeedPostLikeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedposts/{feedPostId}/like")
class FeedPostLikeController(
    private val feedPostLikeService: FeedPostLikeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeFeedPost(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long
    ) {
        feedPostLikeService.likeFeedPost(userId, feedPostId)
    }
}