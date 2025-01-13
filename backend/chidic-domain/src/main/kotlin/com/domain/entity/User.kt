package com.domain.entity

import com.domain.entity.enum.Role
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users")
@SQLRestriction("is_deleted = false")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

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
