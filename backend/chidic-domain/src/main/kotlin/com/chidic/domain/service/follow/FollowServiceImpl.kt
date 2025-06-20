package com.chidic.domain.service.follow

import com.chidic.exception.ExceptionMessage.USER_NOT_FOUND
import com.chidic.domain.entity.Follow
import com.chidic.domain.entity.User
import com.chidic.domain.entity.id.FollowId
import com.chidic.domain.repository.FollowRepository
import com.chidic.domain.repository.UserRepository
import com.chidic.domain.service.user.UserNotFoundException
import com.chidic.dto.FollowCountDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowServiceImpl(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) : FollowService {
    override fun getFollowerAndFolloweeCount(userId: Long): FollowCountDto{
        val targetUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException(USER_NOT_FOUND.message) }

        val followerCount = followRepository.countByFollowee(targetUser)

        val followingCount = followRepository.countByFollower(targetUser)

        return FollowCountDto(
            followerCount = followerCount,
            followingCount = followingCount
        )
    }

    @Transactional
    override fun follow(userId: Long, followingId: Long) {
        val follower = userRepository.getReferenceById(userId)
        val followee = userRepository.getReferenceById(followingId)
        val follow = findOrCreateFollow(FollowId(followerId = userId, followeeId = followingId), follower, followee)

        follow.follow()
    }

    @Transactional
    override fun unfollow(userId: Long, followeeId: Long) {
        val follow = followRepository.findById(FollowId(userId, followeeId))

        follow.get().unFollow()
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