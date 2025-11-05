package com.example.domain.model.nonfeature.terms

enum class TermsStatusType {
    TERMS_UPDATE_REQUIRED, ACTIVE
}

fun String.toTermsAgreement(): TermsStatusType =
    when (this) {
        TermsStatusType.TERMS_UPDATE_REQUIRED.name -> TermsStatusType.TERMS_UPDATE_REQUIRED
        TermsStatusType.ACTIVE.name -> TermsStatusType.ACTIVE
        else -> TermsStatusType.ACTIVE
    }
