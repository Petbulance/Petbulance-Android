package com.example.domain.model.nonfeature.terms

import java.time.Instant

data class Term(
    val id: String,
    val title: String,
    val required: Boolean,
    val summary: String,
    val version: String,
    val lastUpdated: Instant
) {
    companion object {
        fun stub() = Term(
            id = "1",
            title = "어차피 안 읽고 동의할 약관",
            required = true,
            summary = "모두가 안 읽을 것임은 널리 알려진 사실입니다.",
            version = "2025-01-01",
            lastUpdated = Instant.now()
        )
    }
}