package com.chidicdomain.dto

import com.chidicdomain.enum.Provider

data class OAuth2UserInfoDto (
    val username: String,
    val email: String,
    val provider: Provider
)