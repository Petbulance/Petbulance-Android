package com.example.data.repository.feature.hospitals

import com.example.data.remote.network.feature.hospitals.model.HospitalCardResDto
import com.example.data.remote.network.feature.hospitals.model.HospitalDetailResDto
import com.example.data.remote.network.feature.hospitals.model.HospitalsResDto
import com.example.data.remote.network.feature.hospitals.model.OpenHoursDto
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.feature.hospitals.HospitalCard
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.OpenHours
import com.example.domain.model.type.AnimalSpecies

fun HospitalCardResDto.toDomain(): HospitalCard {
    return HospitalCard(
        hospitalId = hospitalId ?: -1,
        name = name.orEmpty(),
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        distanceMeters = distanceMeters ?: 0.0,
        phone = phone.orEmpty(),
        types = types.mapNotNull { runCatching { AnimalSpecies.valueOf(it) }.getOrNull() },
        isOpenNow = isOpenNow ?: false,
        nextOpenHours = nextOpenHours.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0
    )
}

fun HospitalsResDto.toDomain(): Hospital {
    return Hospital(
        hospitalId = hospitalId ?: -1,
        name = name.orEmpty(),
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        distanceMeters = distanceMeters ?: 0.0,
        phone = phone.orEmpty(),
        types = types.mapNotNull { runCatching { AnimalSpecies.valueOf(it) }.getOrNull() },
        isOpenNow = isOpenNow ?: false,
        openHours = openHours.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0
    )
}

fun HospitalDetailResDto.toDomain(): HospitalDetail {
    return HospitalDetail(
        hospitalId = hospitalId ?: -1,
        name = name.orEmpty(),
        address = address.orEmpty(),
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        phone = phone.orEmpty(),
        acceptedAnimals = acceptedAnimals.mapNotNull { runCatching { AnimalSpecies.valueOf(it) }.getOrNull() },
        openHours = openHours.map { it.toDomain() },
        notes = notes.orEmpty(),
        openNow = openNow ?: false,
        description = description.orEmpty()
    )
}

fun OpenHoursDto.toDomain(): OpenHours {
    return OpenHours(
        day = day.orEmpty(),
        hours = hours.orEmpty()
    )
}