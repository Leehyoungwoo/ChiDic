package com.chidicapp.api.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.feedpost.FeedPostService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feedposts")
class FeedPostController(
    private val feedPostService: FeedPostService
) {
    @GetMapping("/newsfeed")
    @ResponseStatus(HttpStatus.OK)
    fun getNewsFeed(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
        @GetUserIdFromPrincipal userId: Long): List<FeedPostListResponse> {
        return feedPostService.getFollowedUsersFeed(userId, page, size)
    }

    @GetMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    fun readFeedPostDetail(
        @PathVariable feedPostId: Long
    ): FeedPostDetailResponse {
        return feedPostService.getFeedPostDetail(feedPostId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createFeed(
        @GetUserIdFromPrincipal userId: Long, @RequestBody feedPostCreateRequest: FeedPostCreateRequest
    ) {
        feedPostService.createFeed(userId, feedPostCreateRequest)
    }

    @PatchMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    // 아무나 고칠 수 없게 preAuthorized추가
    fun updateFeed(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long,
        @RequestBody feedPostUpdateRequest: FeedPostUpdateRequest
    ) {
        feedPostService.updateFeed(feedPostId, feedPostUpdateRequest)
    }

    @DeleteMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    // 아무나 삭제할 수 없게 인가 처리
    fun deleteFeed(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long
    ) {
        feedPostService.deleteFeedPost(feedPostId)
    }
}