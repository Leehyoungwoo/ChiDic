package com.chidic.api.response

import com.chidic.dto.FeedPostListDto

data class FeedPostListResponse(
    val feePosts: List<FeedPostListDto>
)