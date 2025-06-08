package com.chidic.oauth

import com.chidic.dto.OAuth2UserInfoDto

interface OAuthApiClient {
    fun getUserInfo(accessToken: String): OAuth2UserInfoDto
}