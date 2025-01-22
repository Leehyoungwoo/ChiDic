package com.chidicdomain.domain.service.follow

import com.chidiccommon.dto.FollowCountResponse

interface FollowService {
    fun getFollowerAndFolloweeCount(userId: Long): FollowCountResponse
    fun follow(userId: Long, followingId: Long)
    fun unfollow(userId: Long, followeeId: Long)
}