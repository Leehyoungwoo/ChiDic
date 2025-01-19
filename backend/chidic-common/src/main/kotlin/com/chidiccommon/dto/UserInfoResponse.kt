package com.chidiccommon.dto

data class UserInfoResponse (
    val id: Long,
    val username: String,
    val email: String,
    val profilePicture: String?
)