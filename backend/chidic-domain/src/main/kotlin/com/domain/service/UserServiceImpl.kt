package com.domain.service

import com.domain.entity.User
import com.domain.mapper.UserMapper
import com.domain.repository.UserRepository
import com.dto.UserCreateDto
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
    override fun delete(id: Long): Unit {
        var user = userRepository.findById(id).orElseThrow()
        user.deleteData()
    }
}