package com.domain.service

import com.dto.UserCreateDto
import com.dto.UserUpdateDto

interface UserService {
    fun create(user: UserCreateDto): Unit
    fun updateProfileImage(id: Long, userUpdateDto: UserUpdateDto): Unit
    fun delete(id: Long): Unit
}