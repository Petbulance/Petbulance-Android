package com.example.data.remote.network.nonfeature.terms.model.request

import com.example.data.utils.JavaInstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TermsConsentRequest(
    val consents: List<ConsentItem>
)

@Serializable
data class ConsentItem(
    val termId: String,
    val agreed: Boolean,

    @Serializable(with = JavaInstantSerializer::class)
    val updatedAt: Instant?
)