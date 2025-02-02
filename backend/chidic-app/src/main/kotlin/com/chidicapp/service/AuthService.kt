package com.chidicapp.service

import com.chidicapp.security.jwt.util.JwtProvider
import com.chidiccommon.dto.OAuth2UserInfoDto
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.service.user.UserService
import com.chidicdomain.dto.TokenDto
import com.chidicdomain.type.Provider
import com.chidicexternal.oauth.KakaoOAuthApiClient
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val kakaoOAuthApiClient: KakaoOAuthApiClient,
    private val jwtProvider: JwtProvider
) {
    fun loginOrRegister(kakaoAccessToken: String): TokenDto {
        val kakaoUser = kakaoOAuthApiClient.getUserInfo(kakaoAccessToken)
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