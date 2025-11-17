package com.example.data.repository.feature.hospitals

import com.example.data.remote.network.feature.hospitals.HospitalApi
import com.example.domain.model.common.MapBounds
import com.example.domain.model.common.PagedData
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.feature.hospitals.HospitalCard
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.repository.feature.hospitals.HospitalRepository
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val hospitalApi: HospitalApi
) : HospitalRepository {

    override suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: MapBounds?,
        animal: List<AnimalSpecies>?,
        openNow: Boolean?,
        page: Int,
        size: Int
    ): Result<PagedData<Hospital>> {
        val boundsString = bounds?.let { "${it.minLat},${it.minLng},${it.maxLat},${it.maxLng}" }
        return hospitalApi.searchHospitals(
            q = q,
            region = region,
            lat = lat,
            lng = lng,
            bounds = boundsString,
            animal = animal?.map { it.name },
            openNow = openNow,
            page = page,
            size = size
        ).map { pagedResponse ->
            PagedData(
                content = pagedResponse.content.map { it.toDomain() },
                totalPages = pagedResponse.totalPages ?: 0,
                totalElements = pagedResponse.totalElements ?: 0,
                isLast = pagedResponse.last ?: true
            )
        }
    }

    override suspend fun searchHospitalDetail(hospitalId: Long): Result<HospitalDetail> {
        return hospitalApi.searchHospitalDetail(hospitalId).map { it.toDomain() }
    }

    override suspend fun searchHospitalCard(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): Result<HospitalCard> {
        return hospitalApi.searchHospitalCard(hospitalId, lat, lng).map { it.toDomain() }
    }
}