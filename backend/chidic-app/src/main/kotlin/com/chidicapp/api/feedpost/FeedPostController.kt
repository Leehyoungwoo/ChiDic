package com.chidicapp.api.feedpost

import com.chidicapp.api.feedpostlike.FeedPostLikeMapper
import com.chidiccommon.dto.FeedPostCreateRequest
import com.chidicapp.api.response.FeedPostDetailResponse
import com.chidiccommon.dto.FeedPostListResponse
import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.feedpost.FeedPostService
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostUpdateDto
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
        @RequestParam(required = false) lastFeedPostId: Long?,
        @RequestParam(required = false, defaultValue = "20") size: Int,
        @RequestParam(required = false, defaultValue = "0") start: Long,
        @GetUserIdFromPrincipal userId: Long): List<FeedPostListResponse> {
        return feedPostService.getFollowedUsersFeed(userId, lastFeedPostId, size, start)
    }

    @GetMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    fun readFeedPostDetail(
        @PathVariable feedPostId: Long
    ): FeedPostDetailResponse {
        val feedPostDetailDto = feedPostService.getFeedPostDetail(feedPostId)
        return FeedPostMapper.dtoToFeedPostDetailResponse(feedPostDetailDto)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createFeed(
        @GetUserIdFromPrincipal userId: Long, @RequestBody feedPostCreateRequest: FeedPostCreateRequest
    ) {
        val feedPostCreateDto = FeedPostMapper.requestToFeedCreateDto(userId, feedPostCreateRequest)
        feedPostService.createFeed(feedPostCreateDto)
    }

    @PatchMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    // 아무나 고칠 수 없게 preAuthorized추가
    fun updateFeed(
        @GetUserIdFromPrincipal userId: Long,
        @PathVariable feedPostId: Long,
        @RequestBody feedPostUpdateRequest: FeedPostUpdateRequest
    ) {
        val feedPostUpdateDto = FeedPostMapper.requestToFeedPostUpdateDto(feedPostId, feedPostUpdateRequest)
        feedPostService.updateFeed(feedPostUpdateDto)
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

object FeedPostMapper{
    fun dtoToFeedPostDetailResponse(feedPostDetailDto: FeedPostDetailDto): FeedPostDetailResponse {
        return FeedPostDetailResponse(
            title = feedPostDetailDto.title,
            content = feedPostDetailDto.content,
            comments = feedPostDetailDto.comments,
            created = feedPostDetailDto.created
        )
    }

    fun requestToFeedCreateDto(userid: Long, feedPostCreateRequest: FeedPostCreateRequest): FeedPostCreateDto {
        return FeedPostCreateDto(
            userId = userid,
            title = feedPostCreateRequest.title,
            content = feedPostCreateRequest.content,
        )
    }

    fun requestToFeedPostUpdateDto(feedPostId: Long, feedPostUpdateRequest: FeedPostUpdateRequest): FeedPostUpdateDto {
        return FeedPostUpdateDto(
            feedPostId = feedPostId,
            title = feedPostUpdateRequest.title,
            content = feedPostUpdateRequest.content,
        )
    }
}
