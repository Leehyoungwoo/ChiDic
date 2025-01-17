package com.chidicapp.service.auth

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.TokenResponse
import com.chidiccore.jwt.util.JwtProvider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.service.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val kakaoOAuth2Service: KakaoOAuth2Service,
    private val jwtProvider: JwtProvider
) {
    fun loginOrRegister(kakaoAccessToken: String): TokenResponse {
        val kakaoUser = kakaoOAuth2Service.getUserInfo(kakaoAccessToken)

        val user = userService.findUserByUsername(kakaoUser.username) ?: registerUser(kakaoUser)

        val authentication = jwtProvider.getOAuth2Authentication(user)

        val accessToken = jwtProvider.createAccessToken(authentication)
        val refreshToken = jwtProvider.createRefreshToken(authentication)

        // redis에 refreshToken 넣어주는 로직

        return TokenResponse(accessToken, refreshToken)
    }

    private fun registerUser(kakaoUserInfo: OAuth2UserInfo): User {
        return userService.create(kakaoUserInfo)
    }
}