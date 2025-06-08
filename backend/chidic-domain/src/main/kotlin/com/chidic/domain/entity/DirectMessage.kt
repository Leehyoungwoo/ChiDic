package com.chidic.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class DirectMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    val receiver: User,

    @Column(nullable = false, length = 1000)
    val content: String,

    @Column(nullable = false)
    val sendAt: LocalDateTime = LocalDateTime.now()
)
