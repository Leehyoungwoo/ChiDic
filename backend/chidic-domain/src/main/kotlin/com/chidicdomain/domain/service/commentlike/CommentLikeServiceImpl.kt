package com.chidicdomain.domain.service.commentlike

import com.chidicdomain.domain.entity.Comment
import com.chidicdomain.domain.entity.CommentLike
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.entity.id.CommentLikeId
import com.chidicdomain.domain.repository.CommentLikeRepository
import com.chidicdomain.domain.repository.CommentRepository
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentLikeServiceImpl(
    private val commentLikeRepository: CommentLikeRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
): CommentLikeService {
    @Transactional
    override fun likeComment(userId: Long, commentId: Long) {
        val user = userRepository.getReferenceById(userId)
        val comment = commentRepository.getReferenceById(commentId)

        val commentLike = findOrCreateCommentLike(CommentLikeId(userId, commentId), user, comment)

        commentLike.likeComment()
    }

    override fun unlikeComment(userId: Long, commentId: Long) {
        val commentLike = commentLikeRepository.findById(CommentLikeId(userId, commentId))

        commentLike.get().UnlikeComment()
    }

    private fun findOrCreateCommentLike(commentLikeId: CommentLikeId, user: User, comment: Comment): CommentLike {
        return commentLikeRepository.findById(commentLikeId)
            .orElseGet {
                commentLikeRepository.save(
                    CommentLike(
                        id = commentLikeId
                    ).apply {
                        this.user = user
                        this.comment = comment
                    }
                )
            }
    }
}