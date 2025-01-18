package com.chidicdomain.domain.service

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserProfileImageUpdateDto
import com.chidiccommon.dto.UsernameUpdateDto
import com.chidiccommon.exception.ExceptionMessage.USER_NOT_FOUND
import com.chidiccommon.exception.exceptions.UserNotFoundException
import com.chidicdomain.domain.entity.User
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
    override fun findUserByUsername(username: String): User? {
        return userRepository.findByUsername(username).orElse(null)
    }

    @Transactional
    override fun create(oAuth2UserInfo: OAuth2UserInfo): User {
        val newUser = userMapper.toEntity(oAuth2UserInfo)
        return userRepository.save(newUser)
    }

    @Transactional
    override fun updateProfileImage(id: Long, userProfileImageUpdateDto: UserProfileImageUpdateDto): Unit {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateProfileImage(userProfileImageUpdateDto.newImage)
    }

    @Transactional
    override fun updateUsername(id: Long, usernameUpdateDto: UsernameUpdateDto) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateUsername(usernameUpdateDto.username)
    }

    @Transactional
    override fun delete(id: Long): Unit {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.deleteData()
    }
}