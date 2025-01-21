package com.chidicdomain.domain.mapper.user

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserInfoResponse
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User

interface UserMapper {
    fun toEntity(oAuth2UserInfo: OAuth2UserInfo, provider: Provider): User
    fun toInfoDto(user: User): UserInfoResponse
}
