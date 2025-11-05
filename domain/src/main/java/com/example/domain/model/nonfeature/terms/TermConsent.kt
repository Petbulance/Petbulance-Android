package com.example.domain.model.nonfeature.terms

import java.time.Instant

data class TermConsent(
    val consents: List<Consent>
)

data class Consent(
    val termId: String,
    val agreed: Boolean,
    val updatedAt: Instant?
)
