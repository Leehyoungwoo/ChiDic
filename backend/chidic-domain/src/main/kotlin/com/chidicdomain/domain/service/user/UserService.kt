package com.chidicdomain.domain.service.user

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserInfoResponse
import com.chidiccommon.dto.UserProfileImageUpdateRequest
import com.chidiccommon.dto.UsernameUpdateRequest
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User

interface UserService {
    fun getUserInfo(id: Long): UserInfoResponse
    fun findUserByEmailAndProvider(username: String, provider: Provider): User?
    fun create(user: OAuth2UserInfo, provider: Provider): User
    fun updateProfileImage(id: Long, userProfileImageUpdateRequest: UserProfileImageUpdateRequest): Unit
    fun updateUsername(id: Long, usernameUpdateRequest: UsernameUpdateRequest)
    fun delete(id: Long): Unit
}