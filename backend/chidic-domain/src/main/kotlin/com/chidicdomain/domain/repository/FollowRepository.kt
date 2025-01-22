package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.Follow
import com.chidicdomain.domain.entity.id.FollowId
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository: JpaRepository<Follow, FollowId>{
}