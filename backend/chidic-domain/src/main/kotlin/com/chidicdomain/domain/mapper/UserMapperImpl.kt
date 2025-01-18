package com.chidicdomain.domain.mapper

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User
import org.springframework.stereotype.Component

@Component
class UserMapperImpl: UserMapper {
    override fun toEntity(oAuth2UserInfo: OAuth2UserInfo, provider: Provider): User {
        return User(
            email = oAuth2UserInfo.email,
            username = oAuth2UserInfo.username,
            provider = provider
        )
    }
}