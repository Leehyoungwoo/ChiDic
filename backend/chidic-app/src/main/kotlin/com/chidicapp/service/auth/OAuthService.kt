package com.chidicapp.service.auth

import com.chidiccommon.dto.OAuth2UserInfo

interface OAuthService {
    fun getUserInfo(accessToken: String): OAuth2UserInfo
}