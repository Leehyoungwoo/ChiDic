package com.chidicdomain.domain.service.user

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidicapp.api.request.UserProfileImageUpdateRequest
import com.chidiccommon.dto.UsernameUpdateRequest
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.UserInfoDto
import com.chidicdomain.dto.UserProfileUpdateDto
import com.chidicdomain.dto.UsernameUpdateDto

interface UserService {
    fun getUserInfo(id: Long): UserInfoDto
    fun findUserByEmailAndProvider(email: String, provider: Provider): User?
    fun create(user: OAuth2UserInfo): User
    fun updateProfileImage(userProfileUpdateDto: UserProfileUpdateDto): Unit
    fun updateUsername(usernameUpdateDto: UsernameUpdateDto)
    fun delete(id: Long): Unit
    fun getUser(id: Long): User
}