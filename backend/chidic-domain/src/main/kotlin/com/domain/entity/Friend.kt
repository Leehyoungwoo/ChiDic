package com.domain.entity

import com.domain.entity.enum.FriendStatus
import jakarta.persistence.*

@Entity
class Friend (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    var friend: Friend,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var friendStatus: FriendStatus
)