package com.chidic.api.user

import com.chidic.api.request.UserProfileImageUpdateRequest
import com.chidic.api.request.UsernameUpdateRequest
import com.chidic.api.response.UserInfoResponse
import com.chidic.security.auth.model.OAuth2UserDetails
import com.chidic.domain.service.user.UserService
import com.chidic.dto.UserInfoDto
import com.chidic.dto.UserProfileUpdateDto
import com.chidic.dto.UsernameUpdateDto
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun readUserInfo(@AuthenticationPrincipal principal: OAuth2UserDetails): UserInfoResponse {
        val userId = principal.getId()
        val userInfoDto = userService.getUserInfo(userId)
        return UserMapper.toInfoResponse(userInfoDto)
    }

    @PatchMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfileImage(@AuthenticationPrincipal principal: OAuth2UserDetails,
                           @RequestBody userProfileImageUpdateRequest: UserProfileImageUpdateRequest
    ) {
        val userId = principal.getId()
        userService.updateProfileImage(UserMapper.requestToUserUpdateDto(userId, userProfileImageUpdateRequest))
    }

    @PatchMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    fun updateUsername(@AuthenticationPrincipal principal: OAuth2UserDetails,
                       @RequestBody usernameUpdateRequest: UsernameUpdateRequest
    ) {
        val userId = principal.getId()
        userService.updateUsername(UserMapper.requestToUsernameUpdateDto(userId, usernameUpdateRequest))
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@AuthenticationPrincipal principal: OAuth2UserDetails) {
        val userId = principal.getId()
        userService.delete(userId)
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

    fun requestToUsernameUpdateDto(id: Long, usernameUpdateRequest: UsernameUpdateRequest): UsernameUpdateDto {
        return UsernameUpdateDto(
            id = id,
            username = usernameUpdateRequest.username,
        )
    }
}

