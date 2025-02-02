package com.chidicexternal.oauth

import com.chidiccommon.dto.OAuth2UserInfoDto

interface OAuthApiClient {
    fun getUserInfo(accessToken: String): OAuth2UserInfoDto
}