package com.example.domain.repository.nonfeature

interface LogRepository {
    fun printLog(log: String): Result<Unit>

    fun <T> printLog(logObject : T, printMessage: String): Result<Unit>
}