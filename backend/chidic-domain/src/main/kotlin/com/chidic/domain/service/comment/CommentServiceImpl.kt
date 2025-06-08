package com.chidic.domain.service.comment

import com.chidic.exception.ExceptionMessage.COMMENT_NOT_FOUND
import com.chidic.domain.entity.Comment
import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.User
import com.chidic.domain.repository.CommentRepository
import com.chidic.domain.repository.FeedPostRepository
import com.chidic.domain.repository.UserRepository
import com.chidic.dto.CommentCreateDto
import com.chidic.dto.CommentUpdateDto
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