package com.domain.entity

import com.domain.entity.enum.Role
import java.util.UUID
import jakarta.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 100)
    var username: String,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(unique = true, length = 255)
    var profilePicture: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,
): BaseEntity()
