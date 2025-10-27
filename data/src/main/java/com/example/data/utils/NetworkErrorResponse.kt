package com.example.data.utils

import android.util.Log
import com.example.domain.exception.ServerApiException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val status: Int,
    val timestamp: String,
    val success: Boolean,
    val data: ErrorData
)

@Serializable
data class ErrorData(
    val errorClassName: String,
    val message: String
)

/**
 * Ktor API 호출을 안전하게 실행하고, 결과를 Result<T>로 wrapping
 *
 * @param T API 성공 시 반환될 데이터 타입
 * @param apiCall 실제 Ktor API를 호출하는 suspend 람다
 * @return API 호출 결과를 담은 Result<T> 객체.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.success(apiCall())
    } catch (e: ClientRequestException) {
        Result.failure(parseErrorResponse(e.response))
    } catch (e: ServerResponseException) {
        Result.failure(parseErrorResponse(e.response))
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private suspend fun parseErrorResponse(response: HttpResponse): ServerApiException {
    return try {
        val errorResponse = response.body<ApiErrorResponse>()
        Log.e("petbulance", "ApiErrorResponse : \n$ApiErrorResponse")
        ServerApiException(
            message = errorResponse.data.message,
            errorCode = errorResponse.data.errorClassName
        )
    } catch (e: Exception) {
        ServerApiException("An unexpected error occurred")
    }
}
