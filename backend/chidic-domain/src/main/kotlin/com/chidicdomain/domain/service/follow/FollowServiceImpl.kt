package com.chidicdomain.domain.service.follow

import com.chidicdomain.domain.entity.Follow
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.entity.id.FollowId
import com.chidicdomain.domain.repository.FollowRepository
import com.chidicdomain.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowServiceImpl(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) : FollowService {
    @Transactional
    override fun follow(userId: Long, followingId: Long) {
        val follower = userRepository.getReferenceById(userId)
        val followee = userRepository.getReferenceById(followingId)
        val follow = findOrCreateFollow(FollowId(followerId = userId, followeeId = followingId), follower, followee)

        follow.follow()
    }

    private fun findOrCreateFollow(followId: FollowId, follower: User, followee: User): Follow {
        return followRepository.findById(followId)
            .orElseGet {
                Follow(
                    id = followId
                ).apply {
                    this.follower = follower
                    this.followee = followee
                }.also {
                    followRepository.save(it)
                }
            }
    }
}