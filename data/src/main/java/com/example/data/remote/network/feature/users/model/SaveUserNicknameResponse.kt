package com.example.data.remote.network.feature.users.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveUserNicknameResponse(
    val message: String
)