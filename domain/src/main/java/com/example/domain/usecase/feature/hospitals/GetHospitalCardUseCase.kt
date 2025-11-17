package com.example.domain.usecase.feature.hospitals

import com.example.domain.model.feature.hospitals.HospitalCard
import com.example.domain.repository.feature.hospitals.HospitalRepository
import javax.inject.Inject

class GetHospitalCardUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(hospitalId: Long, lat: Double, lng: Double): Result<HospitalCard> {
        return hospitalRepository.searchHospitalCard(hospitalId, lat, lng)
    }
}