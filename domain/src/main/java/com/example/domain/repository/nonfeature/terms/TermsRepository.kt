package com.example.domain.repository.nonfeature.terms

import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.domain.model.nonfeature.terms.TermsStatusType

interface TermsRepository {
    suspend fun getLatestTermsAgreementStatus(): Result<TermsStatusType>

    suspend fun getTerms(): Result<List<Term>>

    suspend fun getTermDetails(termsId: String): Result<TermDetails>

    suspend fun agreeToTerms(termConsent: TermConsent): Result<Unit>
}