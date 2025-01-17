package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByUsername(username: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}