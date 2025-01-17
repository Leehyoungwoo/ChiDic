package com.domain.service

import com.chidiccommon.dto.UserUpdateDto
import com.chidicdomain.domain.entity.User
import com.chidiccommon.enum.Role
import com.chidicdomain.domain.mapper.UserMapper
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.domain.service.UserServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var userMapper: UserMapper

    private lateinit var userService: UserServiceImpl

    @Test
    fun create() {
        // given
        val userCreateDto = UserCreateDto(
            email = "test@google.com",
            username = "테스트인",
        )

        val user = User(
            id = 1,
            username = "테스트인",
            email = "test@google.com",
            profilePicture = null,
            role = Role.USER
        )

        whenever(userMapper.toEntity(userCreateDto)).thenReturn(user)

        userService = UserServiceImpl(userMapper, userRepository)

        userService.create(userCreateDto)

        // then
        verify(userRepository, times(1)).save(any<User>())
    }

    @Test
    fun updateProfileImage() {
        var user = User(
            id = 1,
            username = "테스트인",
            email = "test@google.com",
            profilePicture = null,
            role = Role.USER
        )

        var userUpdateDto = UserUpdateDto(
            newImage = "새로운 이미지"
        )

        whenever(userRepository.findById(1)).thenReturn(Optional.of(user))

        userService = UserServiceImpl(userMapper, userRepository)

        userService.updateProfileImage(1, userUpdateDto)

        verify(userRepository, times(1)).findById(1)
        assertEquals(user.profilePicture, userUpdateDto.newImage)
    }

    @Test
    fun delete() {
        val user = User(
            id = 1,
            username = "테스트인",
            email = "test@google.com",
            profilePicture = null,
            role = Role.USER
        )

        whenever(userRepository.findById(1)).thenReturn(Optional.of(user))

        userService = UserServiceImpl(userMapper, userRepository)

        userService.delete(1)

        assertEquals(true, user.isDeleted)
        verify(userRepository, times(1)).findById(1)
    }
}
