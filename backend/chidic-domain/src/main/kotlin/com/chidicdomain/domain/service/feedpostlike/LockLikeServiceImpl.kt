package com.chidicdomain.domain.service.feedpostlike

import com.chidicdomain.domain.repository.LockRepository
import org.springframework.stereotype.Service

@Service
class LockLikeServiceImpl(
    private val lockRepository: LockRepository,
    private val feedPostLikeService: FeedPostLikeService
) : LockLikeService {
    override fun namedLockLike(userId: Long, feedPostId: Long) {
        try {
            lockRepository.getLock("like_feedpost_$feedPostId")
            feedPostLikeService.likeFeedPost(userId, feedPostId)
        } finally {
            lockRepository.releaseLock("like_feedpost_$feedPostId")
        }
    }

    override fun namedLockUnlike(userId: Long, feedPostId: Long) {
        try {
            lockRepository.getLock("unlike_feedpost_$feedPostId")
            feedPostLikeService.unlikeFeedPost(userId, feedPostId)
        } finally {
            lockRepository.releaseLock("unlike_feedpost_$feedPostId")
        }
    }
}