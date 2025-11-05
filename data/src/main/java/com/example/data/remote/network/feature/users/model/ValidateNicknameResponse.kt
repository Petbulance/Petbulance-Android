package com.example.data.remote.network.feature.users.model

import kotlinx.serialization.Serializable

@Serializable
data class ValidateNicknameResponse(
    val nickname: String,
    val available: Boolean,
    val reason: String?
)
