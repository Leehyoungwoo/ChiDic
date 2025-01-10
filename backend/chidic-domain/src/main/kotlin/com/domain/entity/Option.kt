package com.domain.entity

import jakarta.persistence.*

@Entity
class Option(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    var question: Question,

    @Column(name = "is_corrected", nullable = false)
    var isCorrected: Boolean = false
)