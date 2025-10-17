package com.example.presentation.screen.feature.home

data class HomeData(
    val data: String
) {
    companion object {
        fun empty() = HomeData(
            data = ""
        )
    }
}