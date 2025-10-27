package com.example.domain.model.nonfeature.terms

data class TermDetails(
    val id: String,
    val title: String,
    val required: Boolean,
    val version: String,
    val content: String
)
