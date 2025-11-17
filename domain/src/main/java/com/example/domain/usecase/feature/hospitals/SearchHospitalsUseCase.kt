package com.example.domain.usecase.feature.hospitals

import com.example.domain.model.common.MapBounds
import com.example.domain.model.common.PagedData
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.repository.feature.hospitals.HospitalRepository
import com.example.domain.usecase.nonfeature.PrintLogUseCase
import javax.inject.Inject

class SearchHospitalsUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository,
    private val printLogUseCase: PrintLogUseCase
) {
    /* TODO : debugging */
    suspend operator fun invoke(param: SearchHospitalParams): Result<PagedData<Hospital>> {
        printLogUseCase(param, "Search Hospital UseCase")
//        return hospitalRepository.searchHospitals(
//            q = param.q,
//            region = "${param.region} ${param.district}",
//            lat = param.lat,
//            lng = param.lng,
//            bounds = param.bounds,
//            animal = param.animal,
//            openNow = param.openNowOnly,
//            page = param.page,
//            size = param.size
//        )
        return Result.success(
            PagedData(
                listOf(Hospital.stub()),
                totalPages = 1,
                totalElements = 1,
                isLast = false
            )
        )
    }
}

data class SearchHospitalParams(
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