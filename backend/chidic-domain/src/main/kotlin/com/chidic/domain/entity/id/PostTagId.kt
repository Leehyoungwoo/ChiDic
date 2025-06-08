package com.chidic.domain.entity.id

import jakarta.persistence.Column
import java.io.Serializable

data class PostTagId (
    @Column(name = "post_id")
    val postId: Long,

    @Column(name = "tag_id")
    val tagId: Long
) : Serializable


