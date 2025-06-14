package com.chidic.domain.mapper.user

import com.chidic.dto.OAuth2UserInfoDto
import com.chidic.domain.entity.User
import com.chidic.dto.UserInfoDto
import com.chidic.type.Provider
import org.mapstruct.EnumMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = "spring")
interface UserMapper {
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "role", constant = "USER")
    fun toEntity(oAuth2UserInfDto: OAuth2UserInfoDto): User

    fun toInfoDto(user: User): UserInfoDto

    @EnumMapping(nameTransformationStrategy = MappingConstants.CASE_TRANSFORMATION, configuration = "upper")
    fun mapProvider(provider: String): Provider
}
