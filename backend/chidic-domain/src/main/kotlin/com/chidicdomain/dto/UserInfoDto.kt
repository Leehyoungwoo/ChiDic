package com.chidicdomain.dto

data class UserInfoDto(
    val id: Long,
    val username: String,
    val email: String,
    val profilePicture: String?
)
