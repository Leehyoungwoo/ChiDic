package com.chidicdomain.domain.mapper

import com.chidicdomain.domain.entity.User
import com.chidiccommon.dto.UserCreateDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    fun toEntity(dto: UserCreateDto): User
}