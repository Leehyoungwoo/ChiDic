package com.chidic.domain.service.feedpostlike

interface LockLikeService {
    fun namedLockLike(userId: Long, feedPostId: Long)
    fun namedLockUnlike(userId: Long, feedPostId: Long)
}