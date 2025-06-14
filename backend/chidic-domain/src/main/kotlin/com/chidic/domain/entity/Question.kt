package com.chidic.domain.entity

import com.chidic.type.Category
import jakarta.persistence.*

@Entity
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 1000)
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: Category,

    @Column(nullable = false, length = 1000)
    var explanation: String
)