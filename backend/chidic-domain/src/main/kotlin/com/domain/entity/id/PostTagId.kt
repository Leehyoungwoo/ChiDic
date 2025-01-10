package com.domain.entity.id

import java.io.Serializable

data class PostTagId (
    val postId: Long,
    val tagId: Long
) : Serializable


