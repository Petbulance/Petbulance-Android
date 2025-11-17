package com.example.data.remote.network.nonfeature.login

import com.example.data.remote.network.nonfeature.login.model.request.DataLoginRequest
import com.example.data.remote.network.nonfeature.login.model.response.LoginResponse
import com.example.data.utils.network.BASE_URL
import com.example.data.utils.network.BaseResponse
import com.example.data.utils.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class LoginApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/auth"

    suspend fun signIn(request: DataLoginRequest): Result<LoginResponse> {
        return safeApiCall {
            client.post("$baseUrl/social/login") {
                setBody(request)
            }.body<BaseResponse<LoginResponse>>()
        }
    }
}