package com.example.data.repository.nonfeature.terms

import com.example.data.remote.local.database.nonfeature.terms.TermDetailsEntity
import com.example.data.remote.network.nonfeature.terms.model.request.ConsentItem
import com.example.data.remote.network.nonfeature.terms.model.request.TermsConsentRequest
import com.example.data.remote.network.nonfeature.terms.model.response.TermDetailsResponse
import com.example.data.remote.network.nonfeature.terms.model.response.TermResponse
import com.example.data.remote.network.nonfeature.terms.model.response.TermsStatusResponse
import com.example.domain.model.nonfeature.terms.Consent
import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.domain.model.nonfeature.terms.TermsStatusType


fun TermsStatusResponse.toDomain(): TermsStatusType {
    return when (status) {
        "TERMS_UPDATE_REQUIRED" -> TermsStatusType.TERMS_UPDATE_REQUIRED
        else -> TermsStatusType.ACTIVE
    }
}

fun TermResponse.toDomain(): Term {
    return Term(
        id = id,
        title = title,
        required = required,
        summary = summary,
        version = version,
        lastUpdated = lastUpdated
    )
}

fun TermDetailsResponse.toDomain(): TermDetails {
    return TermDetails(
        id = id,
        title = title,
        required = required,
        version = version,
        content = content
    )
}

fun TermDetailsResponse.toEntity(): TermDetailsEntity {
    return TermDetailsEntity(
        id = id,
        title = title,
        required = required,
        version = version,
        content = content
    )
}

fun TermConsent.toRequest(): TermsConsentRequest {
    return TermsConsentRequest(
        consents = consents.map { it.toRequest() }
    )
}

fun Consent.toRequest(): ConsentItem {
    return ConsentItem(
        termId = termId,
        agreed = agreed,
        updatedAt = updatedAt
    )
}

fun TermDetailsEntity.toDomain() = TermDetails(
    id = id,
    title = title,
    required = required,
    version = version,
    content = content
)

fun TermDetails.toEntity() = TermDetailsEntity(
    id = id,
    title = title,
    required = required,
    version = version,
    content = content
)
