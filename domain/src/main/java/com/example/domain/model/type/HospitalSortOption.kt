package com.example.domain.model.type

enum class HospitalSortOption {
    REVIEWS,
    DISTANCE,
    RATINGS
}

fun HospitalSortOption.toKorean(): String {
    return when (this) {
        HospitalSortOption.REVIEWS -> "리뷰 많은 순"
        HospitalSortOption.DISTANCE -> "가까운 순"
        HospitalSortOption.RATINGS -> "평점 높은 순"
    }
}
