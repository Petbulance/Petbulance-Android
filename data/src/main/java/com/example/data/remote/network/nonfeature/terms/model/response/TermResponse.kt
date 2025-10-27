package com.example.data.remote.network.nonfeature.terms.model.response

import com.example.data.utils.JavaInstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

data class TermResponse(
    val id: String,
    val title: String,
    val required: Boolean,
    val summary: String,
    val version: String,
    @Serializable(with = JavaInstantSerializer::class)
    val lastUpdated: Instant
)