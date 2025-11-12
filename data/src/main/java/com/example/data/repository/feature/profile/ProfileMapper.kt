package com.example.data.repository.feature.profile

import com.example.data.remote.network.feature.users.model.GetUserProfileResponse
import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.model.nonfeature.login.LoginProviderType

fun GetUserProfileResponse.toDomain() = UserProfile(
    provider = LoginProviderType.valueOf(this.provider),
    email = this.email,
    nickname = this.nickname,
    profileImageUrl = this.profileImageUrl ?: "",
    kakaoEmail = this.kakaoEmail,
    googleEmail = this.googleEmail,
    naverEmail = this.naverEmail
)

