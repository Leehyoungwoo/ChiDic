package com.chidiccommon.dto

import com.chidiccommon.enum.Provider

data class OAuth2UserInfo (
    val username: String,
    val email: String,
    val provider: Provider
)