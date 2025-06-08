package com.chidic.domain.mapper.feedpost

import com.chidic.domain.entity.FeedPost
import com.chidic.domain.entity.User
import com.chidic.dto.FeedPostCreateDto
import com.chidic.dto.FeedPostDetailDto
import com.chidic.dto.FeedPostListDto

interface FeedPostMapper {
    fun toEntity(user: User, feedPostCreateDto: FeedPostCreateDto): FeedPost
    fun toFeedPostDetailDto(feedPost: FeedPost): FeedPostDetailDto
    fun toFeedPostListDto(feedPost: FeedPost): FeedPostListDto
}