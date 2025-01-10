package com.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class GroupChat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true, nullable = false)
    var title: String,

    @Column(nullable = false, unique = true, name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
)