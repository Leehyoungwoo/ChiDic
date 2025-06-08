package com.chidic.domain.service.feedpostlike

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.FeedPostLIke
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
import com.chidic.redis.service.RedisService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostLikeServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostLikeRepository: FeedPostLikeRepository,
    private val redisService: RedisService,
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

    @Transactional
    override fun likeFeedPost(userId: Long, feedPostId: Long) {
        val proxyUser = userRepository.getReferenceById(userId)
        val proxyFeedPost = feedPostRepository.getReferenceById(feedPostId)

        val feedPostLike = findOrCreateFeedPostLike(PostLikeId(feedPostId, userId), proxyUser, proxyFeedPost)

        feedPostLike.likePost()

        val newLikeCount = feedPostLike.feedPost!!.likeCount

        // 핫키 판별 및 로컬 캐시 처리
        if (newLikeCount >= hotKeyThreshold) {
            // 핫키인 경우 로컬 캐시에 저장
            localCacheService.putCache(feedPostId.toString(), feedPostMapper.toFeedPostListDto(feedPostLike.feedPost!!))
        }

        feedKafkaProducer.sendFeedLikedEvent(
            LikeEvent(
                feedPostId = feedPostId,
                likeCount = newLikeCount
            )
        )
    }

    @Transactional
    override fun unlikeFeedPost(userId: Long, feedPostId: Long) {
        val feedPostLike = feedPostLikeRepository.findById(PostLikeId(feedPostId, userId))

        feedPostLike.get().unlikePost()

        val likeCountAfterUnlike = feedPostLike.get().feedPost!!.likeCount

        // 핫키 판별 및 로컬 캐시 처리
        if (likeCountAfterUnlike >= hotKeyThreshold) {
            // 핫키인 경우 로컬 캐시 저장
            localCacheService.putCache(
                feedPostId.toString(),
                feedPostMapper.toFeedPostListDto(feedPostLike.get().feedPost!!)
            )
        } else {
            if (localCacheService.getCache(feedPostId.toString()) != null) {
                localCacheService.removeCache(feedPostId.toString())
            }
        }

        val newLikeCount = feedPostLike.get().feedPost!!.likeCount
        feedKafkaProducer.sendFeedUnlikedEvent(
            UnlikeEvent(
                feedPostId = feedPostId,
                likeCount = newLikeCount
            )
        )
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