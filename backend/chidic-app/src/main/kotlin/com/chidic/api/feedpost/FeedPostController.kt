package com.chidic.api.feedpost

import com.chidic.api.request.FeedPostCreateRequest
import com.chidic.api.request.FeedPostUpdateRequest
import com.chidic.api.response.FeedPostDetailResponse
import com.chidic.api.response.FeedPostListResponse
import com.chidic.security.auth.model.OAuth2UserDetails
import com.chidic.domain.service.feedpost.FeedPostService
import com.chidic.dto.FeedPostCreateDto
import com.chidic.dto.FeedPostDetailDto
import com.chidic.dto.FeedPostUpdateDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

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
        @AuthenticationPrincipal principal: OAuth2UserDetails
    ): FeedPostListResponse {
        val userId = principal.getId()
        val followedUsersFeedList = feedPostService.getFollowedUsersFeed(userId, lastFeedPostId, size, start)
        return FeedPostListResponse(
            feePosts = followedUsersFeedList
        )
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
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @Valid @RequestBody feedPostCreateRequest: FeedPostCreateRequest
    ) {
        val userId = principal.getId()
        val feedPostCreateDto = FeedPostMapper.requestToFeedCreateDto(userId, feedPostCreateRequest)
        feedPostService.createFeed(feedPostCreateDto)
    }

    @PatchMapping("/{feedPostId}")
    @ResponseStatus(HttpStatus.OK)
    // 아무나 고칠 수 없게 preAuthorized추가
    fun updateFeed(
        @AuthenticationPrincipal principal: OAuth2UserDetails,
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
        @AuthenticationPrincipal principal: OAuth2UserDetails,
        @PathVariable feedPostId: Long
    ) {
        feedPostService.deleteFeedPost(feedPostId)
    }
}

object FeedPostMapper {
    fun dtoToFeedPostDetailResponse(feedPostDetailDto: FeedPostDetailDto): FeedPostDetailResponse {
        return FeedPostDetailResponse(
            username = feedPostDetailDto.username,
            title = feedPostDetailDto.title,
            content = feedPostDetailDto.content,
            comments = feedPostDetailDto.comments,
            likeCount = feedPostDetailDto.likeCount,
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
