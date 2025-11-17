package com.example.data.utils.network

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    val content: List<T> = emptyList(),
    val pageable: PageableDto? = null,
    val totalPages: Int? = null,
    val totalElements: Long? = null,
    val last: Boolean? = null,
    val first: Boolean? = null,
    val size: Int? = null,
    val number: Int? = null,
    val numberOfElements: Int? = null,
    val empty: Boolean? = null,
    val sort: SortDto? = null
)

@Serializable
data class PageableDto(
    val pageNumber: Int? = null,
    val pageSize: Int? = null,
    val offset: Long? = null,
    val paged: Boolean? = null,
    val unpaged: Boolean? = null,
    val sort: SortDto? = null
)

@Serializable
data class SortDto(
    val sorted: Boolean? = null,
    val unsorted: Boolean? = null,
    val empty: Boolean? = null
)