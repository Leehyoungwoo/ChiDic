package com.chidicapp.security.auth.model

import com.chidicdomain.enum.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserDetails(
    private val id: Long,
    private val username: String,
    private val email: String,
    private val role: Role
): OAuth2User {
    override fun getName(): String {
        return email
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf("email" to username)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role).map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
    }

    fun getRole(): Role {
        return role
    }

    fun getId(): Long {
        return id
    }
}