package com.example.domain.repository.nonfeature.app

interface AppRepository {
    suspend fun getLatestAppVersion(): Result<String>

    suspend fun getCurrentAppVersion(): Result<String>
}