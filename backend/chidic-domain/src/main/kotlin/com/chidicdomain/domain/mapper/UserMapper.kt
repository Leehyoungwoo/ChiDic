package com.chidicdomain.domain.mapper

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidicdomain.domain.entity.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    fun toEntity(oAuth2UserInfo: OAuth2UserInfo): User
}