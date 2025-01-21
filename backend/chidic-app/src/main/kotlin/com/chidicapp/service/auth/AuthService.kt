package com.chidicapp.service.auth

import com.chidiccommon.dto.OAuth2UserInfo
import com.chidiccommon.dto.TokenDto
import com.chidiccommon.enum.Provider
import com.chidiccore.jwt.util.JwtProvider
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.service.user.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val kakaoOAuth2Service: KakaoOAuth2Service,
    private val jwtProvider: JwtProvider
) {
    fun loginOrRegister(kakaoAccessToken: String): TokenDto {
        val kakaoUser = kakaoOAuth2Service.getUserInfo(kakaoAccessToken)

        val user = userService.findUserByEmailAndProvider(kakaoUser.username, Provider.KAKAO) ?: registerUser(kakaoUser)

        val authentication = jwtProvider.getOAuth2Authentication(user)

        val accessToken = jwtProvider.createAccessToken(authentication)
        val refreshToken = jwtProvider.createRefreshToken(authentication)

        return TokenDto(accessToken, refreshToken)
    }

    private fun registerUser(kakaoUserInfo: OAuth2UserInfo): User {
        return userService.create(kakaoUserInfo, Provider.KAKAO)
    }

    fun refreshAccessToken(refreshToken: String): TokenDto {
        jwtProvider.validateToken(refreshToken)

        val authentication = jwtProvider.getOAuth2Authentication(refreshToken)

        val accessToken = jwtProvider.createAccessToken(authentication)
        val newRefreshToken = jwtProvider.createRefreshToken(authentication)

        return TokenDto(accessToken, newRefreshToken)
    }
}