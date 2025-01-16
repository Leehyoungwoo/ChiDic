package com.chidicdomain.domain.entity

import com.chidicdomain.domain.entity.id.GroupChatParticipantId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class GroupChatParticipant(
    @EmbeddedId
    var id: GroupChatParticipantId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupchat_id")
    var groupChat: GroupChat,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    var user: User,

    @Column(nullable = false, name = "join_at")
    var joinAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, name = "is_joined")
    var isJoined: Boolean = true,
)