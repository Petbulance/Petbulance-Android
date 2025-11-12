package com.example.data.remote.network.feature.users.model

import kotlinx.serialization.Serializable

@Serializable
data class GetUserProfileResponse(
    val provider: String,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val kakaoEmail: String?,
    val googleEmail: String?,
    val naverEmail: String?
)