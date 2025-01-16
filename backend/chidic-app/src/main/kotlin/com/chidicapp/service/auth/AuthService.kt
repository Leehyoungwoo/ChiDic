package com.chidicapp.service.auth

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccore.jwt.util.JwtProvider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.service.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val jwtProvider: JwtProvider
) {
    fun loginOrRegister(kakaoUser: OAuth2UserInfo): User {
        return userService.findUserByUsername(kakaoUser.username) ?: registerUser(kakaoUser)
    }

    private fun registerUser(kakaoUser: OAuth2UserInfo): User {
        return User(
            username = kakaoUser.username,
            email = kakaoUser.email,
        )
    }
}