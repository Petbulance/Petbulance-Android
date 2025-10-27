package com.example.domain.exception

data class ServerApiException(
    override val message: String,
    val errorCode: String? = null
) : Exception(message)
