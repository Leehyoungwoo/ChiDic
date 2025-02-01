package com.chidicapp.service.auth

import com.chidicapp.security.jwt.util.JwtProvider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.service.user.UserService
import com.chidicdomain.dto.OAuth2UserInfoDto
import com.chidicdomain.dto.TokenDto
import com.chidicdomain.enum.Provider
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val kakaoOAuth2Service: KakaoOAuth2Service,
    private val jwtProvider: JwtProvider
) {
    fun loginOrRegister(kakaoAccessToken: String): TokenDto {
        val kakaoUser = kakaoOAuth2Service.getUserInfo(kakaoAccessToken)
        val user = userService.findUserByEmailAndProvider(kakaoUser.email, Provider.KAKAO) ?: registerUser(kakaoUser)

        val authentication = jwtProvider.getOAuth2Authentication(user)

        val accessToken = jwtProvider.createAccessToken(authentication)
        val refreshToken = jwtProvider.createRefreshToken(authentication)

        return TokenDto(accessToken, refreshToken)
    }

    private fun registerUser(kakaoUserInfo: OAuth2UserInfoDto): User {
        return userService.create(kakaoUserInfo)
    }

    fun refreshAccessToken(refreshToken: String): TokenDto {
        jwtProvider.validateToken(refreshToken)

        val authentication = jwtProvider.getOAuth2Authentication(refreshToken)

        val accessToken = jwtProvider.createAccessToken(authentication)
        val newRefreshToken = jwtProvider.createRefreshToken(authentication)

        return TokenDto(accessToken, newRefreshToken)
    }

    fun makeTestAccessToken(userId: Long): String {
        val user = userService.getUser(userId)
        return jwtProvider.createTokenFromUser(user, 1800000000)
    }
}