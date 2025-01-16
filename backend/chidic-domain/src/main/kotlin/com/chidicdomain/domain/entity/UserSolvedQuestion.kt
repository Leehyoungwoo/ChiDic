package com.chidicdomain.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class UserSolvedQuestion (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    var question: Question,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_id", nullable = false)
    var selectedOption: Options?,

    @Column(name = "is_corrected", nullable = false)
    var isCorrected: Boolean,

    @Column(name = "solved_at", nullable = false)
    var solvedAt: LocalDateTime = LocalDateTime.now()
)