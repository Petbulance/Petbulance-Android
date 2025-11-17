package com.example.data.remote.network.feature.hospitals.model

import kotlinx.serialization.Serializable

@Serializable
data class HospitalCardResDto(
    val hospitalId: Long?,
    val name: String?,
    val lat: Double?,
    val lng: Double?,
    val distanceMeters: Double?,
    val phone: String?,
    val types: List<String> = emptyList(),
    val isOpenNow: Boolean?,
    val nextOpenHours: String?,
    val thumbnailUrl: String?,
    val rating: Double?,
    val reviewCount: Long?
)