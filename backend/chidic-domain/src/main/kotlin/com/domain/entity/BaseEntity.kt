package com.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Column(nullable = false)
    var isDeleted: Boolean = false
        protected set

    @Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    var updatedAt: LocalDateTime? = null
        protected set

    @PrePersist
    fun prePersist() {
        this.isDeleted = false
        this.createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }

    fun deleteData() {
        this.isDeleted = true
    }
}
