package com.example.domain.model.feature.hospitals

data class HospitalMarker(
    val hospitalId: Long,
    val longitude: Double,
    val latitude: Double,
    val isOpened: Boolean,
    val isSelected: Boolean
)