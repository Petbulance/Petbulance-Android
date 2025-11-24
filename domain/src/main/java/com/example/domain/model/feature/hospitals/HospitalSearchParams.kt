package com.example.domain.model.feature.hospitals

import com.example.domain.model.common.MapBounds
import com.example.domain.model.type.AnimalSpecies

data class HospitalSearchParams(
    val q: String? = null,
    val region: String? = null,
    val district: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val bounds: MapBounds? = null,
    val animal: List<AnimalSpecies>? = null,
    val openNowOnly: Boolean = false,
    val page: Int = 0,
    val size: Int = 10
)