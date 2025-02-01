package com.chidicdomain.domain.mapper.feedpost

import com.chidiccommon.dto.CommentDto
import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
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
}