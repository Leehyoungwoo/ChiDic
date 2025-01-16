package com.chidicdomain.domain.entity

import com.chidiccommon.enum.Provider
import jakarta.persistence.*

@Entity
class SocialAccount (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var provider: Provider
)