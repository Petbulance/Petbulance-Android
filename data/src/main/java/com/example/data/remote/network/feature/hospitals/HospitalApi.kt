package com.example.data.remote.network.feature.hospitals

import com.example.data.remote.network.feature.hospitals.model.HospitalCardResDto
import com.example.data.remote.network.feature.hospitals.model.HospitalDetailResDto
import com.example.data.remote.network.feature.hospitals.model.HospitalsResDto
import com.example.data.utils.network.BASE_URL
import com.example.data.utils.network.BaseResponse
import com.example.data.utils.network.PagedResponse
import com.example.data.utils.network.safeApiCall
import com.example.data.utils.network.safeApiPageCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import jakarta.inject.Inject

class HospitalApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/hospitals"

    suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: String?,
        animal: List<String>?,
        openNow: Boolean?,
        page: Int = 0,
        size: Int = 20
    ): Result<PagedResponse<HospitalsResDto>> {
        return safeApiPageCall {
            client.get("$baseUrl/search") {
                parameter("q", q)
                parameter("region", region)
                parameter("lat", lat)
                parameter("lng", lng)
                parameter("bounds", bounds)
                parameter("animal", animal?.joinToString(","))
                parameter("openNow", openNow)
                parameter("page", page)
                parameter("size", size)
            }.body<BaseResponse<PagedResponse<HospitalsResDto>>>()
        }
    }

    suspend fun searchHospitalDetail(hospitalId: Long): Result<HospitalDetailResDto> {
        return safeApiCall {
            client.get("$baseUrl/$hospitalId").body<BaseResponse<HospitalDetailResDto>>()
        }
    }

    suspend fun searchHospitalCard(hospitalId: Long, lat: Double, lng: Double): Result<HospitalCardResDto> {
        return safeApiCall {
            client.get("$baseUrl/card/$hospitalId") {
                parameter("lat", lat)
                parameter("lng", lng)
            }.body<BaseResponse<HospitalCardResDto>>()
        }
    }
}