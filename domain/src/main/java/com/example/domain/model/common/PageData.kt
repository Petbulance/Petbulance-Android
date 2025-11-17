package com.example.domain.model.common

data class PagedData<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val isLast: Boolean
)