package com.example.data.remote.network.nonfeature.terms.model.response

data class TermDetailsResponse(
    val id: String,
    val title: String,
    val required: Boolean,
    val version: String,
    val content: String
)