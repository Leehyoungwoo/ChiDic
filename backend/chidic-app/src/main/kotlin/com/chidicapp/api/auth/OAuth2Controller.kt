package com.chidicapp.api.auth

import com.chidicapp.service.AuthService
import jakarta.servlet.http.Cookie
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
    fun login(@RequestHeader("X-Kakao-Token") kakaoToken: String, response: HttpServletResponse) {
        val tokenDto = authService.loginOrRegister(kakaoToken)

        CookieUtils.addRefreshTokenCookie(response, tokenDto.refreshToken)
        response.addHeader("Authorization", "Bearer ${tokenDto.accessToken}")
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshToken(
        @CookieValue("refresh_token") refreshToken: String,
        response: HttpServletResponse
    ) {
        val tokenDto = authService.refreshAccessToken(refreshToken)
        CookieUtils.addRefreshTokenCookie(response, tokenDto.refreshToken)
        response.addHeader("Authorization", "Bearer ${tokenDto.accessToken}")
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

object CookieUtils {
    fun addRefreshTokenCookie(response: HttpServletResponse, refreshToken: String) {
        val cookie = Cookie("refresh_token", refreshToken)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/api/refresh"
        cookie.maxAge = 60 * 60 * 24 * 7
        response.addCookie(cookie)
    }

    fun deleteRefreshTokenCookie(response: HttpServletResponse) {
        val cookie = Cookie("refresh_token", "")
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/api/refresh"
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}
