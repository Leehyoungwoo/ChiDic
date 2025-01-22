package com.chidicapp.api.follow

import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.follow.FollowService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/follow/{followeeId}")
class FollowController(
    private val followService: FollowService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun follow(
        @PathVariable followeeId: Long,
        @GetUserIdFromPrincipal userId: Long) {
        followService.follow(userId, followeeId)
    }
}