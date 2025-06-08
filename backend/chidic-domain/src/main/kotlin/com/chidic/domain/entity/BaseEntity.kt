package com.chidic.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
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
        onPrePersist()
    }

    protected fun onPrePersist() {

    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }

    fun deleteData() {
        this.isDeleted = true
    }
}
