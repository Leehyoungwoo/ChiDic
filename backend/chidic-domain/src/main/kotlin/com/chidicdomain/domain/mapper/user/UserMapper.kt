package com.chidicdomain.domain.mapper.user

import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.OAuth2UserInfoDto
import com.chidicdomain.dto.UserInfoDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toEntity(oAuth2UserInfDto: OAuth2UserInfoDto): User
    fun toInfoDto(user: User): UserInfoDto
}
