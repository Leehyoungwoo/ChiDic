package com.chidicdomain.domain.service

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserProfileImageUpdateDto
import com.chidiccommon.dto.UsernameUpdateDto
import com.chidicdomain.domain.entity.User

interface UserService {
    fun findUserByUsername(username: String): User?
    fun create(user: OAuth2UserInfo): User
    fun updateProfileImage(id: Long, userProfileImageUpdateDto: UserProfileImageUpdateDto): Unit
    fun updateUsername(id: Long, usernameUpdateDto: UsernameUpdateDto)
    fun delete(id: Long): Unit
}