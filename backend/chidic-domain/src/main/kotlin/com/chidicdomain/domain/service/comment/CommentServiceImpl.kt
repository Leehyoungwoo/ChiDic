package com.chidicdomain.domain.service.comment

import com.chidiccommon.dto.CommentCreateRequest
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
): CommentService {
    @Transactional
    override fun createComment(feedPostId: Long, userId: Long, commentCreateRequest: CommentCreateRequest) {
        val user = userRepository.getReferenceById(userId)
        val feedPost = feedPostRepository.getReferenceById(feedPostId)

        val newComment = commentMapper.toEntity(user, feedPost, commentCreateRequest)
        commentRepository.save(newComment)
    }
}