package com.example.domain.usecase.nonfeature.app

import com.example.domain.repository.nonfeature.app.AppRepository
import com.example.domain.utils.zip
import javax.inject.Inject

class GetAppVersionsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): Result<Pair<String, String>> = runCatching {
        val (currentVersionResult, latestVersionResult) = zip(
            { repository.getCurrentAppVersion() },
            { repository.getLatestAppVersion() }
        )
        currentVersionResult.getOrThrow() to latestVersionResult.getOrThrow()
    }
}
