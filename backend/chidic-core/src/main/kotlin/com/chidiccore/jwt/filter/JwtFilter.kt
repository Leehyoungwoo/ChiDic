package com.chidiccore.jwt.filter

import com.chidiccore.jwt.util.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
        val token = jwtProvider.resolveToken(request)

        if (!token.isNullOrEmpty() && jwtProvider.validateToken(token!!)) {
            jwtProvider.getOAuth2Authentication(token)?.let {
                SecurityContextHolder.getContext().authentication = it
            }
        }

        filterChain.doFilter(request, response)
    }
}