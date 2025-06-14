package com.chidic.domain.entity

import com.chidic.dto.CommentUpdateDto
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLRestriction("is_deleted = false")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedpost_id", nullable = false)
    var feedPost: FeedPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "content", nullable = false, length = 255)
    var content: String
): BaseEntity() {
    fun updateContent(commentUpdateDto: CommentUpdateDto) {
        content = commentUpdateDto.content
    }
}