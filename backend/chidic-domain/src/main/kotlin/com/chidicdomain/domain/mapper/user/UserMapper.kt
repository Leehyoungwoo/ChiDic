package com.chidicdomain.domain.mapper.user

import com.chidiccommon.dto.OAuth2UserInfoDto
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.UserInfoDto
import com.chidicdomain.enum.Provider
import org.mapstruct.EnumMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    @Mapping(target = "provider", source = "provider")
    fun toEntity(oAuth2UserInfDto: OAuth2UserInfoDto): User

    fun toInfoDto(user: User): UserInfoDto

    @EnumMapping(nameTransformationStrategy = "uppercase")
    fun mapProvider(provider: String): Provider

}
