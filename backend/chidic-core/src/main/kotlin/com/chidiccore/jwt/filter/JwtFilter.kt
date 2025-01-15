package com.chidiccore.jwt.filter

import ch.qos.logback.core.util.StringUtil
import com.chidiccore.jwt.util.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token = jwtProvider.resolveToken(request)

        if (!StringUtil.isNullOrEmpty(token) && jwtProvider.validateToken(token!!)) {
            var authentication = jwtProvider.getOAuth2Authentication(token)

            if (Objects.nonNull(authentication)) {
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}