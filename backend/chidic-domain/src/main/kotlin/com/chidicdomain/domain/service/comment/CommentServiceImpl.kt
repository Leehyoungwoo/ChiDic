package com.chidicdomain.domain.service.comment

import com.chidiccommon.exception.ExceptionMessage.COMMENT_NOT_FOUND
import com.chidicdomain.domain.entity.Comment
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.repository.CommentRepository
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.dto.CommentCreateDto
import com.chidicdomain.dto.CommentUpdateDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
) : CommentService {
    @Transactional
    override fun createComment(commentCreateDto: CommentCreateDto) {
        val user = userRepository.getReferenceById(commentCreateDto.userId)
        val feedPost = feedPostRepository.getReferenceById(commentCreateDto.feedPostId)

        val newComment = CommentEntityMapper.toEntity(user, feedPost, commentCreateDto)
        commentRepository.save(newComment)
    }

    @Transactional
    override fun updateComment(commentUpdateDto: CommentUpdateDto) {
        val comment = commentRepository.findById(commentUpdateDto.commentId)
            .orElseThrow { CommentNotFoundException(COMMENT_NOT_FOUND.message) }
        comment.updateContent(commentUpdateDto)
    }

    @Transactional
    override fun deleteComment(commentId: Long) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException(COMMENT_NOT_FOUND.message) }
        comment.deleteData()
    }
}

object CommentEntityMapper{
    fun toEntity(user: User, feedPost: FeedPost, commentCreateDto: CommentCreateDto): Comment {
        return Comment(
            user = user,
            feedPost = feedPost,
            content = commentCreateDto.content
        )
    }
}