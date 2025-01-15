package com.chidiccore.auth.model

import com.chidicdomain.domain.entity.enum.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserDetails(
    private val username: String,
    private val role: Role
): OAuth2User {
    override fun getName(): String {
        return username
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf("email" to username)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role).map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
    }
}