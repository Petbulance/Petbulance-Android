package com.example.domain.model.nonfeature.login

data class UserInfo(
    val isNewUser: Boolean,
    val signUpToken: String?,
    val firebaseCustomToken: String,
    val accessToken: String,
    val refreshToken: String
)
