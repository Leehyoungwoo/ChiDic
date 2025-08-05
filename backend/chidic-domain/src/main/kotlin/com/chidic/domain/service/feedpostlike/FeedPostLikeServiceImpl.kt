package com.chidic.domain.service.feedpostlike

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.FeedPostLike
import com.chidic.domain.entity.User
import com.chidic.domain.entity.id.PostLikeId
import com.chidic.domain.localCashe.LocalCacheService
import com.chidic.domain.mapper.feedpost.FeedPostMapper
import com.chidic.domain.repository.FeedPostLikeRepository
import com.chidic.domain.repository.FeedPostRepository
import com.chidic.domain.repository.UserRepository
import com.chidic.dto.FeedLikeDto
import com.chidic.kafka.event.LikeEvent
import com.chidic.kafka.event.UnlikeEvent
import com.chidic.kafka.producer.FeedKafkaProducer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostLikeServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostLikeRepository: FeedPostLikeRepository,
    private val feedKafkaProducer: FeedKafkaProducer,
    private val feedPostMapper: FeedPostMapper,
    private val localCacheService: LocalCacheService
) : FeedPostLikeService {
    private val hotKeyThreshold = 500

    override fun getLikeCount(feedPostId: Long): FeedLikeDto {
        val feedPost = feedPostRepository.getReferenceById(feedPostId)
        val postLikes = feedPostLikeRepository.findByFeedPost(feedPost)
        return FeedLikeDto(
            postId = feedPostId,
            likeCount = postLikes.size.toLong()
        )
    }

    override fun likeFeedPost(userId: Long, feedPostId: Long) {
        feedKafkaProducer.sendFeedLikedEvent(
            LikeEvent(
                userId = userId,
                feedPostId = feedPostId
            )
        )
    }

    @Transactional
    override fun createLikeAndIncrementCount(userId: Long, feedPostId: Long) {
        val proxyUser = userRepository.getReferenceById(userId)
        val proxyFeedPost = feedPostRepository.getReferenceById(feedPostId)

        val feedPostLike = findOrCreateFeedPostLike(PostLikeId(feedPostId, userId), proxyUser, proxyFeedPost)

        val newLikeCount = feedPostRepository.incrementLikeCount(feedPostId)

        // 핫키 판별 및 로컬 캐시 처리
        if (newLikeCount >= hotKeyThreshold) {
            // 핫키인 경우 로컬 캐시에 저장
            localCacheService.putCache(feedPostId.toString(), feedPostMapper.toFeedPostListDto(feedPostLike.feedPost!!))
        }
    }

    override fun unlikeFeedPost(userId: Long, feedPostId: Long) {
        feedKafkaProducer.sendFeedUnlikedEvent(
            UnlikeEvent(
                userId = userId,
                feedPostId = feedPostId
            )
        )
    }

    @Transactional
    override fun decrementCount(userId: Long, feedPostId: Long) {
        val feedPostLike = feedPostLikeRepository.findById(PostLikeId(feedPostId, userId)).orElse(null)
            ?: throw IllegalStateException("좋아요가 존재하지 않습니다")
        feedPostLike.unlikePost()

        val likeCountAfterUnlike = feedPostRepository.decrementLikeCount(feedPostId)

        // 핫키 판별 및 로컬 캐시 처리
        if (likeCountAfterUnlike >= hotKeyThreshold) {
            // 핫키가 유지될 경우 ttl 갱신
            localCacheService.putCache(
                feedPostId.toString(),
                feedPostMapper.toFeedPostListDto(feedPostLike.feedPost!!)
            )
        } else {
            // 핫키에서 탈락하면 로컬 캐시에서 제거
            if (localCacheService.getCache(feedPostId.toString()) != null) {
                localCacheService.removeCache(feedPostId.toString())
            }
        }
    }

    private fun findOrCreateFeedPostLike(
        postLikeId: PostLikeId,
        proxyUser: User,
        proxyFeedPost: FeedPost
    ): FeedPostLike {
        return feedPostLikeRepository.findById(postLikeId)
            .orElseGet {
                feedPostLikeRepository.save(
                    FeedPostLike(id = postLikeId).apply {
                        this.user = proxyUser
                        this.feedPost = proxyFeedPost
                    }
                )
            }
    }
}