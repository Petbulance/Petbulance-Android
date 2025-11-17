package com.example.domain.repository.feature.hospitals

import com.example.domain.model.common.MapBounds
import com.example.domain.model.common.PagedData
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.feature.hospitals.HospitalCard
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.type.AnimalSpecies

interface HospitalRepository {
    suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: MapBounds?,
        animal: List<AnimalSpecies>?,
        openNow: Boolean?,
        page: Int = 0,
        size: Int = 20
    ): Result<PagedData<Hospital>>

    suspend fun searchHospitalDetail(hospitalId: Long): Result<HospitalDetail>

    suspend fun searchHospitalCard(hospitalId: Long, lat: Double, lng: Double): Result<HospitalCard>
}