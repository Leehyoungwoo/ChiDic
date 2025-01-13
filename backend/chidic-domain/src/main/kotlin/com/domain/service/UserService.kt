package com.domain.service

import com.domain.entity.User
import com.dto.UserCreateDto

interface UserService {
    fun create(user: UserCreateDto): Unit
    fun delete(id: Long): Unit
}