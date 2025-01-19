package com.chidiccore.jwt.filter

import com.chidiccommon.exception.ExceptionMessage.REFRESH_TOKEN_NOT_FOUND
import com.chidiccommon.exception.exceptions.RefreshTokenNotFoundException
import com.chidiccore.jwt.util.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (isRefreshUrl(request)) {
                handleRefreshToken(request)
            } else {
                handleAccessToken(request)
            }
            filterChain.doFilter(request, response)
        } catch (e: RefreshTokenNotFoundException) {
            response.contentType = "text/plain; charset=UTF-8"
            response.status = HttpStatus.BAD_REQUEST.value()
            response.writer.write(e.message)
        }
    }

    // "/api/refresh" URL 확인
    private fun isRefreshUrl(request: HttpServletRequest): Boolean {
        return request.requestURI.startsWith("/api/refresh")
    }

    // 리프레시 토큰 처리
    private fun handleRefreshToken(request: HttpServletRequest) {
        val refreshToken = getRefreshTokenFromCookies(request)
        if (refreshToken.isNullOrEmpty()) {
            throw RefreshTokenNotFoundException(REFRESH_TOKEN_NOT_FOUND.message)
        }
    }

    // 액세스 토큰 처리
    private fun handleAccessToken(request: HttpServletRequest) {
        val accessToken = jwtProvider.resolveToken(request)
        if (!accessToken.isNullOrEmpty() && jwtProvider.validateToken(accessToken)) {
            jwtProvider.getOAuth2Authentication(accessToken)?.let {
                SecurityContextHolder.getContext().authentication = it
            }
        }
    }

    // 쿠키에서 리프레시 토큰을 가져오는 메서드
    private fun getRefreshTokenFromCookies(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == "refresh_token" }?.value
    }
}
