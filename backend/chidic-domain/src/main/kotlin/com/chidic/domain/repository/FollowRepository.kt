package com.chidic.domain.repository

import com.chidic.domain.entity.Follow
import com.chidic.domain.entity.User
import com.chidic.domain.entity.id.FollowId
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository: JpaRepository<Follow, FollowId>{
    fun countByFollowee(followee: User): Long
    fun countByFollower(follower: User): Long
    fun findAllByFollower(follower: User): List<Follow>
    fun findAllByFollowee(followee: User): List<Follow>
}