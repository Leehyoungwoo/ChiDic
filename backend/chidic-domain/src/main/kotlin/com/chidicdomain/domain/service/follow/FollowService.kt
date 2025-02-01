package com.chidicdomain.domain.service.follow

import com.chidicdomain.dto.FollowCountDto

interface FollowService {
    fun getFollowerAndFolloweeCount(userId: Long): FollowCountDto
    fun follow(userId: Long, followingId: Long)
    fun unfollow(userId: Long, followeeId: Long)
}