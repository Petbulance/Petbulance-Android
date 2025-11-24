package com.example.presentation.screen.feature.hospitals

import android.location.Location
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.feature.hospitals.HospitalSearchParams

data class HospitalSearchData(
    val currentLocation: Location,
    val hospitalsResult: List<Hospital>,
    val hospitalSearchParams: HospitalSearchParams,
    val tempSearchParams: HospitalSearchParams,
    val cameraPosition: Location,
    val currentSelectedHospitalId: Long,
    val isLastPage: Boolean
) {
    companion object {
        fun stub() = HospitalSearchData(
            currentLocation = Location("default").apply {
                latitude = 37.5665
                longitude = 126.9780
            },
            hospitalsResult = listOf(
                Hospital.stub(),
                Hospital.stub().copy(hospitalId = 2L)
            ),
            hospitalSearchParams = HospitalSearchParams(),
            tempSearchParams = HospitalSearchParams(),
            cameraPosition = Location("default").apply {
                latitude = 37.5665
                longitude = 126.9780
            },
            currentSelectedHospitalId = 0L,
            isLastPage = false
        )
    }
}