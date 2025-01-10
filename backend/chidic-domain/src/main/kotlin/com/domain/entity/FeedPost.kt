package com.domain.entity

import jakarta.persistence.*

@Entity
class FeedPost (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "title", nullable = false, length = 50)
    var title: String,

    @Column(name = "content", nullable = false, length = 1000)
    var content: String
)