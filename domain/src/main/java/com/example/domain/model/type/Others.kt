package com.example.domain.model.type

enum class TermsAgreement {
    TERMS_UPDATE_REQUIRED, ACTIVE
}

fun String.toTermsAgreement(): TermsAgreement =
    when (this) {
        TermsAgreement.TERMS_UPDATE_REQUIRED.name -> TermsAgreement.TERMS_UPDATE_REQUIRED
        TermsAgreement.ACTIVE.name -> TermsAgreement.ACTIVE
        else -> TermsAgreement.ACTIVE
    }


enum class LoginProvider {
    NAVER, KAKAO, GOOGLE
}

