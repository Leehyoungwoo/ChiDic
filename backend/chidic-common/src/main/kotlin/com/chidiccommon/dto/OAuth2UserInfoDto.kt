package com.chidiccommon.dto

data class OAuth2UserInfoDto (
    val username: String,
    val email: String,
    val provider: String
)