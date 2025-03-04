package com.chidicdomain.domain.mapper.feedpost

import com.chidicdomain.domain.entity.FeedPost
import com.chidicdomain.domain.entity.User
import com.chidicdomain.dto.FeedPostCreateDto
import com.chidicdomain.dto.FeedPostDetailDto
import com.chidicdomain.dto.FeedPostListDto

interface FeedPostMapper {
    fun toEntity(user: User, feedPostCreateDto: FeedPostCreateDto): FeedPost
    fun toFeedPostDetailDto(feedPost: FeedPost): FeedPostDetailDto
    fun toFeedPostListDto(feedPost: FeedPost): FeedPostListDto
}