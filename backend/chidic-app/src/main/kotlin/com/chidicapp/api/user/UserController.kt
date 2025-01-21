package com.chidicapp.api.user

import com.chidiccommon.dto.UserInfoResponse
import com.chidiccommon.dto.UserProfileImageUpdateDto
import com.chidiccommon.dto.UsernameUpdateDto
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.user.UserService
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
        return userService.getUserInfo(id)
    }

    @PatchMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfileImage(@GetUserIdFromPrincipal id: Long,
                           @RequestBody userProfileImageUpdateDto: UserProfileImageUpdateDto) {
        userService.updateProfileImage(id, userProfileImageUpdateDto)
    }

    @PatchMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    fun updateUsername(@GetUserIdFromPrincipal userId: Long,
                       @RequestBody usernameUpdateDto: UsernameUpdateDto) {
        userService.updateUsername(userId, usernameUpdateDto)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@GetUserIdFromPrincipal id: Long) {
        userService.delete(id)
    }
}