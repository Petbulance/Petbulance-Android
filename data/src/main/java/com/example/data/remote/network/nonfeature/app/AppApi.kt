package com.example.data.remote.network.nonfeature.app

import com.example.data.remote.network.nonfeature.app.model.response.LatestAppVersionResponse
import com.example.data.utils.BASE_URL
import com.example.data.utils.BaseResponse
import com.example.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class AppApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/app"

    suspend fun getLatestAppVersion(): Result<LatestAppVersionResponse> {
        return safeApiCall {
            client.get("$baseUrl/version")
                .body<BaseResponse<LatestAppVersionResponse>>()
        }
    }
}