package com.chidicapp.api.response

import com.chidicdomain.dto.FeedPostListDto

data class FeedPostListResponse(
    val feePosts: List<FeedPostListDto>
)