package com.chidicdomain.domain.repository

import com.chidicdomain.type.Provider
import com.chidicdomain.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmailAndProvider(email: String, provider: Provider): User?
}