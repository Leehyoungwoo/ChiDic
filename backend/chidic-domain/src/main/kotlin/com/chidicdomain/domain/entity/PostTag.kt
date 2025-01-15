package com.chidicdomain.domain.entity

import com.chidicdomain.domain.entity.id.PostTagId
import jakarta.persistence.*

@Entity
class PostTag (
    @EmbeddedId
    var id: PostTagId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false, nullable = false)
    var feedPost: com.chidicdomain.domain.entity.FeedPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false, nullable = false)
    var tag: com.chidicdomain.domain.entity.Tag
)