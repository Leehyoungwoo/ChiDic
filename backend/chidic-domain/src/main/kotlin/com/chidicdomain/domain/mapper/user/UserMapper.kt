package com.chidicdomain.domain.mapper.user

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.UserInfoDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toEntity(oAuth2UserInfo: OAuth2UserInfo): User
    fun toInfoDto(user: User): UserInfoDto
}
