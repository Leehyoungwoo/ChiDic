package com.chidic.domain.service.user

import com.chidic.dto.OAuth2UserInfoDto
import com.chidic.exception.ExceptionMessage.USER_NOT_FOUND
import com.chidic.domain.entity.User
import com.chidic.domain.mapper.user.UserMapper
import com.chidic.domain.repository.UserRepository
import com.chidic.dto.UserInfoDto
import com.chidic.dto.UserProfileUpdateDto
import com.chidic.dto.UsernameUpdateDto
import com.chidic.type.Provider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository
) : UserService {
    override fun getUserInfo(id: Long): UserInfoDto {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        return userMapper.toInfoDto(user)
    }

    override fun findUserByEmailAndProvider(email: String, provider: Provider): User? {
        return userRepository.findByEmailAndProvider(email, provider)
    }

    @Transactional
    override fun create(oAuth2UserInfo: OAuth2UserInfoDto): User {
        val newUser = userMapper.toEntity(oAuth2UserInfo)
        return userRepository.save(newUser)
    }

    @Transactional
    override fun updateProfileImage(userProfileUpdateDto: UserProfileUpdateDto): Unit {
        val user = userRepository.findById(userProfileUpdateDto.id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateProfileImage(userProfileUpdateDto.newImage)
    }

    @Transactional
    override fun updateUsername(usernameUpdateDto: UsernameUpdateDto) {
        val user = userRepository.findById(usernameUpdateDto.id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateUsername(usernameUpdateDto.username)
    }

    @Transactional
    override fun delete(id: Long): Unit {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.deleteData()
    }

    override fun getUser(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
    }
}