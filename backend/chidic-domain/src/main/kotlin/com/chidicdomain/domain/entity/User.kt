package com.chidicdomain.domain.entity

import com.chidiccommon.enum.Role
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

    ): BaseEntity() {
    fun updateProfileImage(newProfileImage: String) {
        this.profilePicture = newProfileImage
    }

    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    override fun onPrePersist() {
        if (role == null) {
            role = Role.USER
        }
    }

    override fun deleteData() {
        super.deleteData()
        username = "deleted_user_${username}"
        email = "deleted_user_${email}"
    }
}
