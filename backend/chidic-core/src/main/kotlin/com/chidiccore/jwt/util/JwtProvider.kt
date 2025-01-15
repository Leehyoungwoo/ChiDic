package com.chidiccore.jwt.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
    @Value("\${jwt.header}") private val jwtHeaderKey: String,
    @Value("\${jwt.secret}") private val jwtSecretKey: String,
    @Value("\${jwt.refresh-token-validity-in-seconds}") private val refreshValidityInMilliseconds: Long,
    @Value("\${jwt.access-token-validity-in-seconds}") private val accessValidityInMilliseconds: Long

) {
    private val AUTHORIZES_KEY = "authorities"
    private lateinit var key: SecretKey

    @PostConstruct
    fun afterPropertiesSet() {
        this.key = SecretKeySpec(Base64.getDecoder().decode(jwtSecretKey), "HmacSHA256")
    }

    fun createAccessToken(authentication: Authentication): String {
        return createToken(authentication, this.accessValidityInMilliseconds)
    }

    fun createRefreshToken(authentication: Authentication): String {
        return createToken(authentication, this.refreshValidityInMilliseconds)
    }

    fun getOAuth2Authentication(token: String): Authentication? {
        var claims: Claims? = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        var username = claims?.get("username").toString()

        return UsernamePasswordAuthenticationToken(null, null, null)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
            return true
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: ExpiredJwtException) {
            return false
        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return false
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request?.getHeader(jwtHeaderKey)
    }

    private fun createToken(
        authentication: Authentication,
        tokenValidityInMilliseconds: Long): String {
        return Jwts.builder()
            .claim(AUTHORIZES_KEY, authentication.authorities
                .stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(",")))
            .signWith(key)
            .expiration(Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
            .compact()
    }

    private fun getAuthorities(claims: Claims?): Any {
        return claims?.get(AUTHORIZES_KEY)
            ?.toString()
            ?.split(",")
            ?.map {it.trim()}
            ?.filter { it.isNotBlank() }
            ?.map(::SimpleGrantedAuthority)
            .orEmpty()
    }
}