package com.example.data.remote.local.database.nonfeature.preference

import com.example.domain.repository.nonfeature.preference.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val provider: PreferenceProvider
) : PreferenceRepository {
    override fun getTheme(): Flow<Result<String>> {
        return provider.observeTheme().map { Result.success(it) }
    }

    override suspend fun updateTheme(theme: String): Result<Unit> {
        return runCatching { provider.updateTheme(theme) }
    }
}