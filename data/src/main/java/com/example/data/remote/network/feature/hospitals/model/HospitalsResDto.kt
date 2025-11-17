package com.example.data.remote.network.feature.hospitals.model

import kotlinx.serialization.Serializable

@Serializable
data class HospitalsResDto(
    val hospitalId: Long? = null,
    val name: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val distanceMeters: Double? = null,
    val phone: String? = null,
    val types: List<String> = emptyList(),
    val isOpenNow: Boolean? = null,
    val openHours: String? = null,
    val thumbnailUrl: String? = null,
    val rating: Double? = null,
    val reviewCount: Long? = null
)