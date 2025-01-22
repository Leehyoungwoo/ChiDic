package com.chidicapp.api.follow

import com.chidiccommon.dto.FollowCountResponse
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.follow.FollowService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followService: FollowService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun followerAndFolloweeCount(
        @GetUserIdFromPrincipal userId: Long
    ): FollowCountResponse {
        return followService.getFollowerAndFolloweeCount(userId)
    }


    @PostMapping("/{followeeId}")
    @ResponseStatus(HttpStatus.OK)
    fun follow(
        @PathVariable followeeId: Long,
        @GetUserIdFromPrincipal userId: Long
    ) {
        followService.follow(userId, followeeId)
    }

    @DeleteMapping("/{followeeId}")
    @ResponseStatus(HttpStatus.OK)
    fun unfollow(
        @PathVariable followeeId: Long,
        @GetUserIdFromPrincipal userId: Long
    ) {
        followService.unfollow(userId, followeeId)
    }
}