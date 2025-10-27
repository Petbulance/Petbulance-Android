package com.example.data.remote.network.nonfeature.users

import com.example.data.remote.network.nonfeature.users.model.request.DataLoginRequest
import com.example.data.utils.BASE_URL
import com.example.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class LoginResponse(
    val isNewUser: Boolean,
    val signUpToken: String? = null,
    val firebaseCustomToken: String,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

class LoginApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/auth"

    suspend fun signIn(request: DataLoginRequest): Result<LoginResponse> {
        return safeApiCall {
            client.post("$baseUrl/social/login") {
                setBody(request)
            }.body<LoginResponse>()
        }
    }
}