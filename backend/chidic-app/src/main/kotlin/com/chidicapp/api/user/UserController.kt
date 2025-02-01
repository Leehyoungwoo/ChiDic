package com.chidicapp.api.user

import com.chidicapp.api.response.UserInfoResponse
import com.chidicapp.api.request.UserProfileImageUpdateRequest
import com.chidiccommon.dto.CommentRequest
import com.chidiccommon.dto.UsernameUpdateRequest
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.user.UserService
import com.chidicdomain.dto.UserInfoDto
import com.chidicdomain.dto.UserProfileUpdateDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun readUserInfo(@GetUserIdFromPrincipal id: Long): UserInfoResponse {
        val userInfoDto = userService.getUserInfo(id)
        return UserMapper.toInfoResponse(userInfoDto)
    }

    @PatchMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfileImage(@GetUserIdFromPrincipal id: Long,
                           @RequestBody userProfileImageUpdateRequest: UserProfileImageUpdateRequest
    ) {
        userService.updateProfileImage(UserMapper.requestToUserUpdateDto(id, userProfileImageUpdateRequest))
    }

    @PatchMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    fun updateUsername(@GetUserIdFromPrincipal userId: Long,
                       @RequestBody usernameUpdateRequest: UsernameUpdateRequest) {
        userService.updateUsername(userId, usernameUpdateRequest)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@GetUserIdFromPrincipal id: Long) {
        userService.delete(id)
    }
}

object UserMapper{
    fun toInfoResponse(userInfoDto: UserInfoDto): UserInfoResponse {
        return UserInfoResponse(
            id = userInfoDto.id,
            username = userInfoDto.username,
            email = userInfoDto.email,
            profilePicture = userInfoDto.profilePicture
        )
    }

    fun requestToUserUpdateDto(id: Long, request: UserProfileImageUpdateRequest): UserProfileUpdateDto {
        return UserProfileUpdateDto(
            id = id,
            newImage = request.newImage
        )
    }
}

