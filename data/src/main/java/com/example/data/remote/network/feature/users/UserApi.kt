package com.example.data.remote.network.feature.users

import com.example.data.remote.network.feature.users.model.GetUserProfileResponse
import com.example.data.remote.network.feature.users.model.RequestTemporaryNicknameResponse
import com.example.data.remote.network.feature.users.model.SaveUserNicknameRequest
import com.example.data.remote.network.feature.users.model.SaveUserNicknameResponse
import com.example.data.remote.network.feature.users.model.ValidateNicknameResponse
import com.example.data.utils.BASE_URL
import com.example.data.utils.BaseResponse
import com.example.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import jakarta.inject.Inject

class UserApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/users"

    suspend fun saveNickname(request: SaveUserNicknameRequest): Result<SaveUserNicknameResponse> {
        return safeApiCall {
            client.post("$baseUrl/social/login") {
                setBody(request)
            }.body<BaseResponse<SaveUserNicknameResponse>>()
        }
    }

    suspend fun requestTemporaryNickname(): Result<RequestTemporaryNicknameResponse> {
        return safeApiCall {
            client.get("$baseUrl/random-nickname")
                .body<BaseResponse<RequestTemporaryNicknameResponse>>()
        }
    }

    suspend fun validateNickname(nickname: String): Result<ValidateNicknameResponse> {
        return safeApiCall {
            client.get("$baseUrl/nickname") {
                parameter("nickname", nickname)
            }.body<BaseResponse<ValidateNicknameResponse>>()
        }
    }

    suspend fun getUserProfile(): Result<GetUserProfileResponse> {
        return safeApiCall {
            client.get("$baseUrl/me").body<BaseResponse<GetUserProfileResponse>>()
        }
    }
}