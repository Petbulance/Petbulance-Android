package com.example.data.remote.network.nonfeature.terms

import com.example.data.remote.network.nonfeature.terms.model.TermsStatusResponse
import com.example.data.utils.BASE_URL
import com.example.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class TermsApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/terms"

    suspend fun getLatestTermsAgreementStatus(): Result<TermsStatusResponse> {
        return safeApiCall {
            client.get("$baseUrl/status").body<TermsStatusResponse>()
        }
    }

}