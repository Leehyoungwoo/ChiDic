package com.chidicdomain.domain.entity

import com.domain.entity.enum.Provider
import jakarta.persistence.*
import java.util.*

@Entity
class SocialAccount (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: com.chidicdomain.domain.entity.User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var provider: Provider
)