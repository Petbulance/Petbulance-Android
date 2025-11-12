package com.example.data.repository.nonfeature.app

import com.example.domain.repository.nonfeature.app.AppRepository
import javax.inject.Inject

class MockAppRepository @Inject constructor() : AppRepository {

    override suspend fun getLatestAppVersion(): Result<String> {
        return Result.success("v1.2")
    }

    override suspend fun getCurrentAppVersion(): Result<String> {
        return Result.success("v1.2")
    }

}