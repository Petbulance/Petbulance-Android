package com.example.domain.model.feature.hospitals

import com.example.domain.model.type.AnimalSpecies

data class HospitalDetail(
    val hospitalId: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val acceptedAnimals: List<AnimalSpecies>,
    val openHours: List<OpenHours>,
    val notes: String,
    val openNow: Boolean,
    val description: String
)

data class OpenHours(
    val day: String,
    val hours: String
)