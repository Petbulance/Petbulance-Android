package com.example.data.remote.network.nonfeature.users.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val isNewUser: Boolean,
    val signUpToken: String? = null,
    val firebaseCustomToken: String,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
