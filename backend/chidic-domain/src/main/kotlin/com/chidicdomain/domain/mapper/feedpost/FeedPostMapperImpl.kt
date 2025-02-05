package com.chidicdomain.domain.mapper.feedpost

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.CommentDto
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostListDto
import org.springframework.stereotype.Component

@Component
class FeedPostMapperImpl: FeedPostMapper {
    override fun toEntity(user: User, feedPostCreateDto: FeedPostCreateDto): FeedPost {
        return FeedPost(
            user = user,
            title = feedPostCreateDto.title,
            content = feedPostCreateDto.content
        )
    }

    override fun toFeedPostDetailDto(feedPost: FeedPost): FeedPostDetailDto {
        return FeedPostDetailDto(
            title = feedPost.title,
            content = feedPost.content,
            created = feedPost.createdAt,
            comments = feedPost.comments.map {
                CommentDto(
                    userId = it.user.username,
                    content = it.content,
                    createdTime = it.createdAt
                )
            }.toMutableList()
        )
    }

    override fun toFeedPostListDto(feedPost: FeedPost): FeedPostListDto {
        return FeedPostListDto(
            feedPostId = feedPost.id,
            title = feedPost.title,
            content = feedPost.content,
            writer = feedPost.user.username,
            writerProfile = feedPost.user.profilePicture,
            likeCount = feedPost.likeCount,
            commentCount = feedPost.comments.size.toLong(),
            createdAt = feedPost.createdAt
        )
    }
}