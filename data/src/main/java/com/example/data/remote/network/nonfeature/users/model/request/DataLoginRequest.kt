package com.example.data.remote.network.nonfeature.users.model.request

import com.example.domain.utils.login.DomainLoginRequest
import kotlinx.serialization.Serializable

@Serializable
data class DataLoginRequest(
    val provider: String,
    val authCode: String
)

fun DomainLoginRequest.toData(): DataLoginRequest {
    return DataLoginRequest(
        provider = this.platform,
        authCode = this.accessToken
    )
}