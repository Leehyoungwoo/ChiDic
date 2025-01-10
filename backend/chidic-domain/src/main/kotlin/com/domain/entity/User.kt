package com.domain.entity

import com.domain.entity.enum.Role
import java.util.UUID
import jakarta.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 100)
    val username: String,

    @Column(nullable = false, unique = true, length = 255)
    val email: String,

    @Column(unique = true, length = 255)
    val profilePicture: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,
)
