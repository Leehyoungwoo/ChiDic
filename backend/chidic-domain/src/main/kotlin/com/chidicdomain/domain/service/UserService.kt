package com.chidicdomain.domain.service

import com.chidiccommon.dto.UserCreateDto
import com.chidiccommon.dto.UserUpdateDto

interface UserService {
    fun create(user: UserCreateDto): Unit
    fun updateProfileImage(id: Long, userUpdateDto: UserUpdateDto): Unit
    fun delete(id: Long): Unit
}