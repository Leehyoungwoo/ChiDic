package com.chidicapp.api.follow

import com.chidicapp.api.response.FollowCountResponse
import com.chidicapp.security.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.follow.FollowService
import com.chidicdomain.dto.FollowCountDto
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
        val followCountDto = followService.getFollowerAndFolloweeCount(userId)
        return FollowMapper.dtoToFollowCountResponse(followCountDto)
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

object FollowMapper {
    fun dtoToFollowCountResponse(followCountDto: FollowCountDto): FollowCountResponse {
        return FollowCountResponse(
            followerCount = followCountDto.followerCount,
            followingCount = followCountDto.followingCount,
        )
    }
}