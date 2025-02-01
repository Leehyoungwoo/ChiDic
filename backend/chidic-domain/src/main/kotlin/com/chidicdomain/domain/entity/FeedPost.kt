package com.chidicdomain.domain.entity

import com.chidiccommon.dto.FeedPostUpdateRequest
import com.chidicdomain.dto.FeedPostUpdateDto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLRestriction("is_deleted = false")
class FeedPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("hibernateLazyInitializer", "handler") // 이 부분 추가
    var user: User,

    @Column(name = "title", nullable = false, length = 50)
    var title: String,

    @Column(name = "content", nullable = false, length = 1000)
    var content: String,

    @OneToMany(mappedBy = "feedPost", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var comments: MutableList<Comment> = mutableListOf()
): BaseEntity() {
    fun updateFeedPost(feedPostUpdateDto: FeedPostUpdateDto) {
        title = feedPostUpdateDto.title
        content = feedPostUpdateDto.content
    }
}
