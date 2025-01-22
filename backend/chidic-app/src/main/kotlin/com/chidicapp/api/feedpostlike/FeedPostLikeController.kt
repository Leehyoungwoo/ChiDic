package com.chidicapp.api.feedpostlike

import com.chidiccommon.dto.FeedLikeResponse
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.feedpostlike.FeedPostLikeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedposts/{feedPostId}/like")
class FeedPostLikeController(
    private val feedPostLikeService: FeedPostLikeService
) {
    // 내가 좋아요 했는 여부 만들어야함
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeCount(
        @PathVariable feedPostId: Long
    ) : FeedLikeResponse{
        return feedPostLikeService.getLikeCount(feedPostId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeFeedPost(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long
    ) {
        feedPostLikeService.likeFeedPost(userId, feedPostId)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun unlikeFeedPost(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long
    ) {
        feedPostLikeService.unlikeFeedPost(userId, feedPostId)
    }
}