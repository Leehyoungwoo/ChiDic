package com.chidicdomain.domain.service

import com.chidiccommon.dto.UserCreateDto
import com.chidiccommon.dto.UserUpdateDto
import com.chidicdomain.domain.mapper.UserMapper
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository
) : UserService {

    @Transactional
    override fun create(userCreateDto: UserCreateDto): Unit {
        val newUser = userMapper.toEntity(userCreateDto)
        userRepository.save(newUser)
    }

    @Transactional
    override fun updateProfileImage(id: Long, userUpdateDto: UserUpdateDto): Unit {
        var user = userRepository.findById(id).orElseThrow()
        user.updateProfileImage(userUpdateDto.newImage)
    }

    @Transactional
    override fun delete(id: Long): Unit {
        var user = userRepository.findById(id).orElseThrow()
        user.deleteData()
    }
}