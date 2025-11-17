package com.example.data.repository.nonfeature

import android.util.Log
import com.example.domain.repository.nonfeature.LogRepository
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor() : LogRepository {

    override fun printLog(log: String): Result<Unit> = runCatching {
        Log.d("siria22 [Log]", log)
    }

    override fun <T> printLog(logObject: T, printMessage: String): Result<Unit> = runCatching {
        Log.d("siria22 [Log]", "$logObject : $printMessage")
    }
}