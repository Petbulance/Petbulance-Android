package com.example.domain.model.feature.users

data class DomainValidateNickname(
    val available: Boolean,
    val reason: String?
)
