package com.chidic.domain.entity.id

import jakarta.persistence.Column
import java.io.Serializable

data class PostLikeId (
    @Column(name = "post_id")
    var postId: Long?,

    @Column(name = "user_id")
    var userId: Long?
) : Serializable {
    constructor(): this(null, null)
}
