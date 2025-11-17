package com.example.domain.usecase.nonfeature

import com.example.domain.repository.nonfeature.LogRepository
import javax.inject.Inject

class PrintLogUseCase @Inject constructor(
    private val repository: LogRepository
) {
    operator fun invoke(log: String): Result<Unit> {
        return repository.printLog(log)
    }

    operator fun <T> invoke(logObject: T, logMessage: String) : Result<Unit> {
        return repository.printLog(logObject, logMessage)
    }
}
