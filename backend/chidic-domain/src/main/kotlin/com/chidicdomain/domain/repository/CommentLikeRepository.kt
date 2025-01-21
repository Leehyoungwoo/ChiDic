package com.chidicdomain.domain.repository

import com.chidicdomain.domain.entity.CommentLike
import com.chidicdomain.domain.entity.id.CommentLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository : JpaRepository<CommentLike, CommentLikeId> {
}