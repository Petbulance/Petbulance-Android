package com.example.data.remote.network.feature.hospitals.model

import kotlinx.serialization.Serializable

@Serializable
data class HospitalDetailResDto(
    val hospitalId: Long? = null,
    val name: String? = null,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val phone: String? = null,
    val acceptedAnimals: List<String> = emptyList(),
    val openHours: List<OpenHoursDto> = emptyList(),
    val notes: String? = null,
    val openNow: Boolean? = null,
    val description: String? = null
)

@Serializable
data class OpenHoursDto(
    val day: String? = null,
    val hours: String? = null
)