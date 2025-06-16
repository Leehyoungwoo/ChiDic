package com.chidic.domain.service.feedpost

import com.chidic.exception.ExceptionMessage.FEED_POST_NOT_FOUND
import com.chidic.domain.localCashe.LocalCacheService
import com.chidic.domain.mapper.feedpost.FeedPostMapper
import com.chidic.domain.repository.FeedPostRepository
import com.chidic.domain.repository.FollowRepository
import com.chidic.domain.repository.UserRepository
import com.chidic.dto.FeedPostCreateDto
import com.chidic.dto.FeedPostDetailDto
import com.chidic.dto.FeedPostListDto
import com.chidic.dto.FeedPostUpdateDto
import com.chidic.kafka.event.FeedCreatedEvent
import com.chidic.kafka.producer.FeedKafkaProducer
import com.chidic.lock.DistributedLockExecutor
import com.chidic.redis.service.RedisService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedPostServiceImpl(
    private val userRepository: UserRepository,
    private val feedPostRepository: FeedPostRepository,
    private val feedPostMapper: FeedPostMapper,
    private val followRepository: FollowRepository,
    private val redisService: RedisService,
    private val feedKafkaProducer: FeedKafkaProducer,
    private val localCacheService: LocalCacheService,
    private val lockExecutor: DistributedLockExecutor
) : FeedPostService {
    private val hotKeyThreshold = 500

    override fun getFollowedUsersFeed(
        userId: Long,
        lastFeedPostId: Long?,
        size: Int,
        start: Long
    ): List<FeedPostListDto> {
        // Redis에서 feedPostId 리스트 조회
        val cachedFeedPostIds = redisService.getFeedPostIdsForUser(userId, lastFeedPostId, size)

        // 캐시 미스일 경우 DB에서 조회
        if (cachedFeedPostIds.isEmpty()) {
            return fetchAndCacheFeedFromDB(userId, size)
        }

        // Redis에서 개별 feedPost 조회 & 캐시 미스 처리
        val finalFeedPosts = getFeedPostsFromCacheOrDB(cachedFeedPostIds)

        // 읽음 처리 (Redis 업데이트)
        if (finalFeedPosts.isNotEmpty()) {
            redisService.markReadAsFeed(userId, cachedFeedPostIds)
        }

        return finalFeedPosts.sortedByDescending { it.feedPostId }
    }

    override fun getFeedPostDetail(feedPostId: Long): FeedPostDetailDto {
        val feedPost = feedPostRepository.findFeedPostWithUserAndComments(feedPostId)
            ?: throw FeedPostNotFoundException(FEED_POST_NOT_FOUND.message)
        return feedPostMapper.toFeedPostDetailDto(feedPost)
    }

    @Transactional
    override fun createFeed(feedPostCreateDto: FeedPostCreateDto) {
        val proxyUser = userRepository.getReferenceById(feedPostCreateDto.userId)

        val newFeedPost = feedPostMapper.toEntity(proxyUser, feedPostCreateDto)

        feedPostRepository.save(newFeedPost)

        // 팔로우하고 있는 userId에 sorted Set들에 게시물을 저장
        // Reids Hash에 Dto로 변환하여 Redis로 넘겨야 함
        // Redis는 캐싱 역할만 담당
        val followers = followRepository.findAllByFollowee(proxyUser)
        val feedPostListDto = feedPostMapper.toFeedPostListDto(newFeedPost)

        feedKafkaProducer.sendFeedCreatedEvent(
            FeedCreatedEvent(
                userIds = followers.map { it.follower!!.id },
                feedPostListDto = feedPostListDto
            )
        )
    }

    @Transactional
    override fun updateFeed(feedPostUpdateDto: FeedPostUpdateDto) {
        val feedPost = feedPostRepository.findById(feedPostUpdateDto.feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.updateFeedPost(feedPostUpdateDto)

        feedKafkaProducer.sendFeedUpdatedEvent(feedPostUpdateDto)
    }

    @Transactional
    override fun deleteFeedPost(feedPostId: Long) {
        val feedPost = feedPostRepository.findById(feedPostId)
            .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
        feedPost.deleteData()
        redisService.deleteFeedPostHash(feedPostId)
    }

    /**
     *  캐시 미스 시 DB에서 데이터를 가져와 Redis에 캐싱
     */
    private fun fetchAndCacheFeedFromDB(userId: Long, size: Int): List<FeedPostListDto> {
        val readFeedPostIds = redisService.getReadMarkList(userId)

        val followList = followRepository.findAllByFollower(userRepository.getReferenceById(userId))
        val userList = followList.mapNotNull { it.followee }

        val postsFromDb = feedPostRepository.findUnreadFeedPosts(
            userList,
            readFeedPostIds.map { it.toLong() },
            PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"))
        )

        // 디비에서 가져온 피드도 읽음 처리
        redisService.markReadAsFeed(userId, postsFromDb.map { it.id })

        return postsFromDb.map { post ->
            val dto = feedPostMapper.toFeedPostListDto(post)
            feedKafkaProducer.sendFeedCreatedEvent(
                FeedCreatedEvent(
                    userIds = userList.map { it.id },
                    feedPostListDto = dto
                )
            )
            dto
        }
    }

    /**
     *  Redis에서 개별 게시글 조회 & 캐시 미스 처리
     */
    private fun getFeedPostsFromCacheOrDB(cachedFeedPostIds: List<Long>): List<FeedPostListDto> {
        val feedPostLists = mutableListOf<FeedPostListDto>()

        cachedFeedPostIds.forEach { feedPostId ->
            val cachedFeedPost = localCacheService.getCache(feedPostId.toString()) as? FeedPostListDto
                ?: redisService.getFeedPostFromHash(feedPostId)

            if (cachedFeedPost != null) {
                feedPostLists += cachedFeedPost
                return@forEach
            }

            val feedPostListDto = lockExecutor.execute(
                key = "lock:feedPost:$feedPostId",
                cache = { redisService.getFeedPostFromHash(feedPostId) },
                critical = {
                    val entity = feedPostRepository.findById(feedPostId)
                        .orElseThrow { FeedPostNotFoundException(FEED_POST_NOT_FOUND.message) }
                    feedPostMapper.toFeedPostListDto(entity).also(redisService::saveFeedPostDtoToHash)
                }
            )

            feedPostLists += feedPostListDto
        }

        return feedPostLists
    }
}

