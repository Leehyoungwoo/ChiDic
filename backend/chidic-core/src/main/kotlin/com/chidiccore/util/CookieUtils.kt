package com.chidiccore.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

class CookieUtils {

    companion object{
        fun addRefreshTokenCookie(response: HttpServletResponse, refreshToken: String) {
            val cookie = Cookie("refresh_token", refreshToken)
            cookie.isHttpOnly = true
            cookie.secure = true
            cookie.path = "/api/refresh"
            cookie.maxAge = 60 * 60 * 24 * 7
            response.addCookie(cookie)
        }
    }
}