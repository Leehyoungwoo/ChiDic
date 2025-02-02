package com.chidicapp.security.jwt.util

import com.chidicapp.security.auth.model.OAuth2UserDetails
import com.chidicdomain.type.Role
import com.chidicdomain.domain.entity.User
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
    private val BEARER_PREFIX = "Bearer "

    @PostConstruct
    fun afterPropertiesSet() {
        key = SecretKeySpec(Base64.getDecoder().decode(jwtSecretKey), "HmacSHA256")
    }

    fun createAccessToken(authentication: Authentication): String {
        return createToken(authentication, this.accessValidityInMilliseconds)
    }

    fun createRefreshToken(authentication: Authentication): String {
        return createToken(authentication, this.refreshValidityInMilliseconds)
    }

    fun getOAuth2Authentication(token: String): Authentication {
        val claims: Claims? = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        val principal = OAuth2UserDetails(
            id = claims?.get("id")?.toString()?.toLongOrNull() ?: 0L,
            username = claims?.get("username").toString(),
            role = Role.valueOf(claims?.get("role").toString()),
            email = claims?.get("email").toString()
        )

        return UsernamePasswordAuthenticationToken(principal, token, principal.authorities)
    }

    fun getOAuth2Authentication(user: User): Authentication {
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val principal = OAuth2UserDetails(
            id = user.id,
            username = user.username,
            role = user.role,
            email = user.email
        )

        return UsernamePasswordAuthenticationToken(principal, null, authorities)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
            return true
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
            return false
        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return false
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(jwtHeaderKey)?.takeIf { it.startsWith(BEARER_PREFIX) }?.removePrefix(BEARER_PREFIX)
    }

    fun createTokenFromUser(
        user: User,
        tokenValidityInMilliseconds: Long
    ): String {
        return Jwts.builder()
            .claim("id", user.id)
            .claim("username", user.username)
            .claim("role", user.role.toString())
            .signWith(key)
            .setExpiration(Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
            .compact()
    }


    private fun createToken(
        authentication: Authentication,
        tokenValidityInMilliseconds: Long
    ): String {
        return Jwts.builder()
            .claim(AUTHORIZES_KEY, authentication.authorities
                .stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(",")))
            .claim("id", (authentication.principal as OAuth2UserDetails).getId())
            .claim("role", (authentication.principal as OAuth2UserDetails).getRole().toString())
            .signWith(key)
            .expiration(Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
            .compact()
    }

    private fun getAuthorities(claims: Claims?): Any {
        return claims?.get(AUTHORIZES_KEY)
            ?.toString()
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?.map(::SimpleGrantedAuthority)
            .orEmpty()
    }
}