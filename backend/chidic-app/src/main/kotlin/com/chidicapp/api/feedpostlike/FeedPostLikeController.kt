package com.chidicapp.api.feedpostlike

import com.chidicapp.api.response.FeedLikeResponse
import com.chidicapp.security.auth.model.OAuth2UserDetails
import com.chidicdomain.domain.service.feedpostlike.FeedPostLikeService
import com.chidicdomain.dto.FeedLikeDto
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedposts/{feedPostId}/like")
class FeedPostLikeController(
    private val feedPostLikeService: FeedPostLikeService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeCount(
        @PathVariable feedPostId: Long
    ) : FeedLikeResponse {
        val feedLikeDto = feedPostLikeService.getLikeCount(feedPostId)
        return FeedPostLikeMapper.dtoToFeedLikeResponse(feedLikeDto)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun likeFeedPost(
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @PathVariable feedPostId: Long
    ) {
        val userId = principal.getId()
        feedPostLikeService.likeFeedPost(userId, feedPostId)
    }

    // 내가 좋아요 했는 여부 만들어야함
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun unlikeFeedPost(
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @PathVariable feedPostId: Long
    ) {
        val userId = principal.getId()
        feedPostLikeService.unlikeFeedPost(userId, feedPostId)
    }
}

object FeedPostLikeMapper {
    fun dtoToFeedLikeResponse(feedLikeDto: FeedLikeDto): FeedLikeResponse {
        return FeedLikeResponse(
            postId = feedLikeDto.postId,
            likeCount = feedLikeDto.likeCount
        )
    }
}