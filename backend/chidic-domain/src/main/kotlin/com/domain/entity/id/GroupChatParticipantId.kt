package com.domain.entity.id

import java.io.Serializable

data class GroupChatParticipantId (
    var groupChatId: Long,
    var groupChatUserId: Long
) : Serializable