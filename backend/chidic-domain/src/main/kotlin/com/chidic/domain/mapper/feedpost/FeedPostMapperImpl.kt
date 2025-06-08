package com.chidic.domain.mapper.feedpost

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.User
import com.chidic.dto.CommentDto
import com.chidic.dto.FeedPostCreateDto
import com.chidic.dto.FeedPostDetailDto
import com.chidic.dto.FeedPostListDto
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
            username = feedPost.user.username,
            title = feedPost.title,
            content = feedPost.content,
            created = feedPost.createdAt,
            likeCount = feedPost.likeCount,
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