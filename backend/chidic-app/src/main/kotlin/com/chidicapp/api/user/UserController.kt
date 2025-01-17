package com.chidicapp.api.user

import com.chidiccommon.dto.UserUpdateDto
import com.chidiccore.auth.annotatiton.GetUserIdFromPrincipal
import com.chidicdomain.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PatchMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfileImage(@GetUserIdFromPrincipal id: Long,
                           @RequestBody userUpdateDto: UserUpdateDto) {
        userService.updateProfileImage(id, userUpdateDto)
    }
}