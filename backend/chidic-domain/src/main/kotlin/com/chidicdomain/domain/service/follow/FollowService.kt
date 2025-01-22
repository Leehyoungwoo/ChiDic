package com.chidicdomain.domain.service.follow

interface FollowService {
    fun follow(userId: Long, followingId: Long)
}