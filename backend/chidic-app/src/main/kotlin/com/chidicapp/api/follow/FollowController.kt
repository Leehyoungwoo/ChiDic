package com.chidicapp.api.follow

import com.chidicapp.api.response.FollowCountResponse
import com.chidicapp.security.auth.model.OAuth2UserDetails
import com.chidicdomain.domain.service.follow.FollowService
import com.chidicdomain.dto.FollowCountDto
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followService: FollowService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun followerAndFolloweeCount(
        @AuthenticationPrincipal principal: OAuth2UserDetails
    ): FollowCountResponse {
        val userId = principal.getId()
        val followCountDto = followService.getFollowerAndFolloweeCount(userId)
        return FollowMapper.dtoToFollowCountResponse(followCountDto)
    }


    @PostMapping("/{followeeId}")
    @ResponseStatus(HttpStatus.OK)
    fun follow(
        @PathVariable followeeId: Long,
        @AuthenticationPrincipal principal: OAuth2UserDetails
    ) {
        val userId = principal.getId()
        followService.follow(userId, followeeId)
    }

    @DeleteMapping("/{followeeId}")
    @ResponseStatus(HttpStatus.OK)
    fun unfollow(
        @PathVariable followeeId: Long,
        @AuthenticationPrincipal principal: OAuth2UserDetails
    ) {
        val userId = principal.getId()
        followService.unfollow(userId, followeeId)
    }
}

object FollowMapper {
    fun dtoToFollowCountResponse(followCountDto: FollowCountDto): FollowCountResponse {
        return FollowCountResponse(
            followerCount = followCountDto.followerCount,
            followingCount = followCountDto.followingCount,
        )
    }
}