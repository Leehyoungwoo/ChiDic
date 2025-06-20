package com.chidic.domain.repository

import com.chidic.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
}