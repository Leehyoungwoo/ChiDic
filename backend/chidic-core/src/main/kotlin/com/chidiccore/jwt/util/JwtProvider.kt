package com.chidiccore.jwt.util

import com.chidiccore.auth.model.OAuth2UserDetails
import com.chidiccommon.enum.Role
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
import java.text.SimpleDateFormat
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

    fun getOAuth2Authentication(token: String): Authentication? {
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

    private fun createToken(
        authentication: Authentication,
        tokenValidityInMilliseconds: Long
    ): String {
        val currentTimeMillis = System.currentTimeMillis()
        val expirationTime = currentTimeMillis + tokenValidityInMilliseconds
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedTime = dateFormat.format(Date(currentTimeMillis))
        println("현재 시간: $formattedTime")
        val newFomattedTime = dateFormat.format(Date(expirationTime))
        println("더해진 시간: $newFomattedTime")
        System.out.println(java.util.TimeZone.getDefault().getID());
        return Jwts.builder()
            .claim(AUTHORIZES_KEY, authentication.authorities
                .stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(",")))
            .claim("id", (authentication.principal as OAuth2UserDetails).getId())
            .signWith(key)
            .expiration(Date(expirationTime))
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