package com.chidicdomain.domain.service

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserInfoResponse
import com.chidiccommon.dto.UserProfileImageUpdateDto
import com.chidiccommon.dto.UsernameUpdateDto
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User

interface UserService {
    fun getUserInfo(id: Long): UserInfoResponse
    fun findUserByEmailAndProvider(username: String, provider: Provider): User?
    fun create(user: OAuth2UserInfo, provider: Provider): User
    fun updateProfileImage(id: Long, userProfileImageUpdateDto: UserProfileImageUpdateDto): Unit
    fun updateUsername(id: Long, usernameUpdateDto: UsernameUpdateDto)
    fun delete(id: Long): Unit
}