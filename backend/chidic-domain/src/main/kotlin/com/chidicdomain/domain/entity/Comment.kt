package com.chidicdomain.domain.entity

import jakarta.persistence.*

@Entity
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedpost_id", nullable = false)
    var feedpost: com.chidicdomain.domain.entity.FeedPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: com.chidicdomain.domain.entity.User,

    @Column(name = "content", nullable = false, length = 255)
    var content: String
)