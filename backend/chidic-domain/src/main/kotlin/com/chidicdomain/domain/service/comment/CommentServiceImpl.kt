package com.chidicdomain.domain.service.comment

import com.chidiccommon.dto.CommentRequest
import com.chidiccommon.exception.ExceptionMessage.*
import com.chidiccommon.exception.exceptions.CommentNotFoundException
import com.chidicdomain.domain.mapper.comment.CommentMapper
import com.chidicdomain.domain.repository.CommentRepository
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val commentMapper: CommentMapper
) : CommentService {
    @Transactional
    override fun createComment(feedPostId: Long, userId: Long, commentRequest: CommentRequest) {
        val user = userRepository.getReferenceById(userId)
        val feedPost = feedPostRepository.getReferenceById(feedPostId)

        val newComment = commentMapper.toEntity(user, feedPost, commentRequest)
        commentRepository.save(newComment)
    }

    @Transactional
    override fun updateComment(commentId: Long, commentRequest: CommentRequest) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException(COMMENT_NOT_FOUND.message) }
        comment.updateContent(commentRequest)
    }

    @Transactional
    override fun deleteComment(commentId: Long) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException(COMMENT_NOT_FOUND.message) }
        comment.deleteData()
    }
}