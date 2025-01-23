package com.chidicapp.api.auth

import com.chidicapp.service.auth.AuthService
import com.chidiccommon.dto.TokenResponse
import com.chidiccore.util.CookieUtils
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(("/api"))
class OAuth2Controller(
    private val authService: AuthService
) {
    @PostMapping("/login/kakao")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestHeader("X-Kakao-Token") kakaoToken: String, response: HttpServletResponse): TokenResponse {
        val tokenDto = authService.loginOrRegister(kakaoToken)

        CookieUtils.addRefreshTokenCookie(response, tokenDto.refreshToken)

        return TokenResponse(
            accessToken = tokenDto.accessToken
        )
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshToken(
        @CookieValue("refresh_token") refreshToken: String,
        response: HttpServletResponse
    ): TokenResponse {
        val tokenDto = authService.refreshAccessToken(refreshToken)

        CookieUtils.addRefreshTokenCookie(response, tokenDto.refreshToken)

        return TokenResponse(
            accessToken = tokenDto.accessToken
        )
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    fun logout(response: HttpServletResponse) {
        CookieUtils.deleteRefreshTokenCookie(response)
    }

    @PostMapping("/make-access-token/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun makeAccessToken(
        @PathVariable("userId") userId: Long
    ): String {
        return authService.makeTestAccessToken(userId)
    }
}