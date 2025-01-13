package com.domain.mapper

import com.domain.entity.User
import com.dto.UserCreateDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    fun toEntity(dto: UserCreateDto): User
}