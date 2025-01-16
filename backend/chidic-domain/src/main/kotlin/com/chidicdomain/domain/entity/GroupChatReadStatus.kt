package com.chidicdomain.domain.entity

import jakarta.persistence.*

@Entity
class GroupChatReadStatus (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupchat_id", nullable = false)
    var groupChat: GroupChat,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
)