package com.example.data.repository.nonfeature.app

import android.content.Context
import com.example.data.remote.network.nonfeature.app.AppApi
import com.example.domain.repository.nonfeature.app.AppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appApi: AppApi
) : AppRepository {

    override suspend fun getLatestAppVersion(): Result<String> {
        return appApi.getLatestAppVersion().map { response ->
            response.version
        }
    }

    override suspend fun getCurrentAppVersion(): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: ""
        }
    }

}