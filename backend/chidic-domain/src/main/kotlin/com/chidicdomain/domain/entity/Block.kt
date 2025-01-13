package com.chidicdomain.domain.entity

import jakarta.persistence.*

@Entity
class Block(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: com.chidicdomain.domain.entity.User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    var blockedUser: com.chidicdomain.domain.entity.User
)