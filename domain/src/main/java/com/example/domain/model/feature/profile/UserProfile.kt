package com.example.domain.model.feature.profile

import com.example.domain.model.nonfeature.login.LoginProviderType

data class UserProfile(
    val provider: LoginProviderType,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val kakaoEmail: String?,
    val googleEmail: String?,
    val naverEmail: String?
) {
    companion object {
        fun stub() = UserProfile(
            provider = LoginProviderType.KAKAO,
            email = "petbulanceTest@kakao.com",
            nickname = "집에가고싶다",
            profileImageUrl = "",
            kakaoEmail = "petbulanceTest@kakao.com",
            googleEmail = null,
            naverEmail = "somenaveremail@naver.com"
        )
    }
}