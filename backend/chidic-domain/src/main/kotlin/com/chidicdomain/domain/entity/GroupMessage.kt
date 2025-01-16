package com.chidicdomain.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class GroupMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groupchat_id", nullable = false)
    var groupChat: GroupChat,

    @Column(name = "content", nullable = false, length = 255)
    var content: String,

    @Column(name = "send_at", nullable = false)
    var sendAt: LocalDateTime = LocalDateTime.now(),
)