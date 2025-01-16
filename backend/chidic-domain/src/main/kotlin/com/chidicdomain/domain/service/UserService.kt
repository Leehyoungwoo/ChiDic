package com.chidicdomain.domain.service

import com.chidiccommon.dto.UserCreateDto
import com.chidiccommon.dto.UserUpdateDto
import com.chidicdomain.domain.entity.User

interface UserService {
    fun findUserByUsername(username: String): User?
    fun create(user: UserCreateDto): Unit
    fun updateProfileImage(id: Long, userUpdateDto: UserUpdateDto): Unit
    fun delete(id: Long): Unit
}