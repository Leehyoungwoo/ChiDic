package com.chidicapp.service.auth

import com.chidicdomain.dto.OAuth2UserInfoDto

interface OAuthService {
    fun getUserInfo(accessToken: String): OAuth2UserInfoDto
}