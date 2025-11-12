package com.example.presentation.screen.feature.mypage

import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.model.nonfeature.login.LoginProviderType

data class MyPageData(
    val userProfile: UserProfile?,
    val currentAppVersion: String,
    val latestAppVersion: String,
) {
    companion object {
        fun empty() = MyPageData(
            userProfile = UserProfile(
                provider = LoginProviderType.KAKAO,
                email = "user@example.com",
                nickname = "따뜻한햄스터07",
                profileImageUrl = "",
                kakaoEmail = "someemail@kakao.com",
                googleEmail = null,
                naverEmail = null
            ),
            currentAppVersion = "1.0.0",
            latestAppVersion = "1.3.0"
        )
    }
}