package com.domain.entity.id

import java.io.Serializable
import java.util.UUID

data class GroupChatParticipantId (
    var groupChatId: Long,
    var groupChatUserId: UUID
) : Serializable