package com.chidic.domain.repository

import com.chidic.domain.entity.CommentLike
import com.chidic.domain.entity.id.CommentLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository : JpaRepository<CommentLike, CommentLikeId> {
}