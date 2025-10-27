package com.example.data.remote.network.nonfeature.terms

import com.example.data.remote.network.nonfeature.terms.model.request.TermsConsentRequest
import com.example.data.remote.network.nonfeature.terms.model.response.TermDetailsResponse
import com.example.data.remote.network.nonfeature.terms.model.response.TermResponse
import com.example.data.remote.network.nonfeature.terms.model.response.TermsStatusResponse
import com.example.data.utils.BASE_URL
import com.example.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    suspend fun getTerms(): Result<List<TermResponse>> {
        return safeApiCall {
            client.get(baseUrl).body<List<TermResponse>>()
        }
    }

    suspend fun getTermDetails(termsId: String): Result<TermDetailsResponse> {
        return safeApiCall {
            client.get("$baseUrl/$termsId").body<TermDetailsResponse>()
        }
    }

    suspend fun postConsents(request: TermsConsentRequest): Result<Unit> {
        return safeApiCall {
            client.post("$baseUrl/consents") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<Unit>()
        }
    }
}