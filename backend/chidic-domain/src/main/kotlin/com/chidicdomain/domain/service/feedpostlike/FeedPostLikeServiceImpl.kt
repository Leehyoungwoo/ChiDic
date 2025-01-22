package com.chidicdomain.domain.service.feedpostlike

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.FeedPostLIke
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.entity.id.PostLikeId
import com.chidicdomain.domain.repository.FeedPostLikeRepository
import com.chidicdomain.domain.repository.FeedPostRepository
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostLikeServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostLikeRepository: FeedPostLikeRepository
) : FeedPostLikeService {

    @Transactional
    override fun likeFeedPost(userId: Long, feedPostId: Long) {
        val proxyUser = userRepository.getReferenceById(userId)

        val proxyFeedPost = feedPostRepository.getReferenceById(feedPostId)

        val feedPostLike = findOrCreateFeedPostLike(PostLikeId(feedPostId, userId), proxyUser, proxyFeedPost)

        feedPostLike.likePost()
    }

    private fun findOrCreateFeedPostLike(
        postLikeId: PostLikeId,
        proxyUser: User,
        proxyFeedPost: FeedPost
    ): FeedPostLIke {
        return feedPostLikeRepository.findById(postLikeId)
            .orElseGet {
                feedPostLikeRepository.save(
                    FeedPostLIke(id = postLikeId).apply {
                        this.user = proxyUser
                        this.feedPost = proxyFeedPost
                    }
                )
            }
    }
}