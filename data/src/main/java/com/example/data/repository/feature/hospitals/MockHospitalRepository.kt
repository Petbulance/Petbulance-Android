package com.example.data.repository.feature.hospitals

import com.example.domain.model.common.MapBounds
import com.example.domain.model.common.PagedData
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.feature.hospitals.HospitalCard
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.OpenHours
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.repository.feature.hospitals.HospitalRepository
import javax.inject.Inject

class MockHospitalRepository @Inject constructor() : HospitalRepository {
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
        val totalPages = 5
        val totalElements = totalPages * size

        if (page >= totalPages) {
            return Result.success(
                PagedData(
                    content = emptyList(),
                    totalPages = totalPages,
                    totalElements = totalElements.toLong(),
                    isLast = true
                )
            )
        }

        val baseLat = lat ?: 37.5
        val baseLng = lng ?: 127.0

        val hospitals = List(size) { idx ->
            val hospitalId = (page * size + idx).toLong()
            val offset = hospitalId * 0.001
            Hospital(
                hospitalId = hospitalId,
                name = "가상 병원 ${hospitalId + 1}",
                lat = baseLat + offset,
                lng = baseLng + offset,
                distanceMeters = (100 + hospitalId * 50).toDouble(),
                phone = "02-1111-22%02d".format(hospitalId),
                types = listOf(AnimalSpecies.RABBIT, AnimalSpecies.GECKO),
                isOpenNow = hospitalId % 3 != 0L,
                openHours = "10:00-19:00",
                thumbnailUrl = "https://cdn.example.com/hosp/$hospitalId/thumb.jpg",
                rating = 4.8,
                reviewCount = (20 + hospitalId)
            )
        }

        return Result.success(
            PagedData(
                content = hospitals,
                totalPages = totalPages,
                totalElements = totalElements.toLong(),
                isLast = page == totalPages - 1
            )
        )
    }

    override suspend fun searchHospitalDetail(hospitalId: Long): Result<HospitalDetail> {
        return Result.success(
            HospitalDetail(
                hospitalId = hospitalId,
                name = "모킹동물병원 상세",
                address = "서울시 종로구 상세로 123",
                lat = 37.57,
                lng = 126.98,
                phone = "02-7777-7777",
                acceptedAnimals = listOf(AnimalSpecies.HAMSTER, AnimalSpecies.PARROT),
                openHours = listOf(
                    OpenHours("MON", "09:00-19:00"),
                    OpenHours("TUE", "09:00-17:00")
                ),
                notes = "매주 일요일/공휴일 휴진(예약진료)",
                openNow = true,
                description = "국내 최고의 모킹 동물병원입니다."
            )
        )
    }

    override suspend fun searchHospitalCard(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): Result<HospitalCard> {
        return Result.success(
            HospitalCard(
                hospitalId = hospitalId,
                name = "모킹동물병원 카드",
                lat = lat,
                lng = lng,
                distanceMeters = 123.0,
                phone = "02-7777-7777",
                types = listOf(AnimalSpecies.HAMSTER, AnimalSpecies.PARROT),
                isOpenNow = true,
                nextOpenHours = "10:00-19:00",
                thumbnailUrl = "https://cdn.example.com/hosp/$hospitalId/thumb.jpg",
                rating = 4.6,
                reviewCount = 32
            )
        )
    }
}