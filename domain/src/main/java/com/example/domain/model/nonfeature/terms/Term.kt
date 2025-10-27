package com.example.domain.model.nonfeature.terms

import java.time.Instant

data class Term(
    val id: String,
    val title: String,
    val required: Boolean,
    val summary: String,
    val version: String,
    val lastUpdated: Instant
)