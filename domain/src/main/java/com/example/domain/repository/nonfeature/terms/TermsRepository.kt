package com.example.domain.repository.nonfeature.terms

import com.example.domain.model.type.TermsAgreement

interface TermsRepository {
    suspend fun getLatestTermsAgreementStatus(): Result<TermsAgreement>
}