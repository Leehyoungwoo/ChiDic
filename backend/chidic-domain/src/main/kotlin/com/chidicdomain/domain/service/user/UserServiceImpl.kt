package com.chidicdomain.domain.service.user

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.UserInfoResponse
import com.chidiccommon.dto.UserProfileImageUpdateRequest
import com.chidiccommon.dto.UsernameUpdateRequest
import com.chidiccommon.enum.Provider
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
    override fun getUserInfo(id: Long): UserInfoResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        return userMapper.toInfoDto(user)
    }

    override fun findUserByEmailAndProvider(email: String, provider: Provider): User? {
        return userRepository.findByEmailAndProvider(email, provider)
    }

    @Transactional
    override fun create(oAuth2UserInfo: OAuth2UserInfo, provider: Provider): User {
        val newUser = userMapper.toEntity(oAuth2UserInfo, provider)
        return userRepository.save(newUser)
    }

    @Transactional
    override fun updateProfileImage(id: Long, userProfileImageUpdateRequest: UserProfileImageUpdateRequest): Unit {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateProfileImage(userProfileImageUpdateRequest.newImage)
    }

    @Transactional
    override fun updateUsername(id: Long, usernameUpdateRequest: UsernameUpdateRequest) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.updateUsername(usernameUpdateRequest.username)
    }

    @Transactional
    override fun delete(id: Long): Unit {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }
        user.deleteData()
    }
}