package com.chidic.domain.service.user

import com.chidic.dto.OAuth2UserInfoDto
import com.chidic.domain.entity.User
import com.chidic.dto.UserInfoDto
import com.chidic.dto.UserProfileUpdateDto
import com.chidic.dto.UsernameUpdateDto
import com.chidic.type.Provider

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