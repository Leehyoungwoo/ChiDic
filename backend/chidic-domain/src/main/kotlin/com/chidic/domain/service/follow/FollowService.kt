package com.chidic.domain.service.follow

import com.chidic.dto.FollowCountDto

interface FollowService {
    fun getFollowerAndFolloweeCount(userId: Long): FollowCountDto
    fun follow(userId: Long, followingId: Long)
    fun unfollow(userId: Long, followeeId: Long)
}