package com.example.presentation.screen.nonfeature.welcome

data class WelcomeData(
    val tempNickname: String
) {
    companion object {
        fun empty() = WelcomeData(
            tempNickname = "행복한 햄스터 67"
        )
    }
}