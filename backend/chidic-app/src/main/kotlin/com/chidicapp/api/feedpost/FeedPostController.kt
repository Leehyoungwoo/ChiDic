package com.chidicapp.api.feedpost

import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.feedpost.FeedPostService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/feedposts")
class FeedPostController(
    private val feedPostService: FeedPostService
) {
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
}