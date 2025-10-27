package com.example.domain.repository.nonfeature.login

interface TokenRepository {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
}