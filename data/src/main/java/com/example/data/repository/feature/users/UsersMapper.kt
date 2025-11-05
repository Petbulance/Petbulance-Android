package com.example.data.repository.feature.users

import com.example.data.remote.network.feature.users.model.ValidateNicknameResponse
import com.example.domain.model.feature.users.DomainValidateNickname

fun ValidateNicknameResponse.toDomain(): DomainValidateNickname {
    return DomainValidateNickname(
        available = this.available,
        reason = this.reason
    )
}
