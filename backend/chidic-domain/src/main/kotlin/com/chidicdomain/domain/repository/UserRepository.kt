package com.chidicdomain.domain.repository

import com.chidiccommon.enum.Provider
import com.chidicdomain.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmailAndProvider(email: String, provider: Provider): User?
}