package com.chidic.domain.repository

import com.chidic.type.Provider
import com.chidic.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmailAndProvider(email: String, provider: Provider): User?
}