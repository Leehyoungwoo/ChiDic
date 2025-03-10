package com.chidicdomain.domain.service.user

import com.chidiccommon.dto.OAuth2UserInfoDto
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.UserInfoDto
import com.chidicdomain.dto.UserProfileUpdateDto
import com.chidicdomain.dto.UsernameUpdateDto
import com.chidicdomain.type.Provider

interface UserService {
    fun getUserInfo(id: Long): UserInfoDto
    fun findUserByEmailAndProvider(email: String, provider: Provider): User?
    fun create(user: OAuth2UserInfoDto): User
    fun updateProfileImage(userProfileUpdateDto: UserProfileUpdateDto): Unit
    fun updateUsername(usernameUpdateDto: UsernameUpdateDto)
    fun delete(id: Long): Unit
    fun getUser(id: Long): User
}

class UserNotFoundException(message: String) : RuntimeException(message)