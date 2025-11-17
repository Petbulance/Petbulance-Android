package com.example.domain.usecase.feature.hospitals

import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.repository.feature.hospitals.HospitalRepository
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(hospitalId: Long): Result<HospitalDetail> {
        return hospitalRepository.searchHospitalDetail(hospitalId)
    }
}