package com.example.domain.usecase.nonfeature.terms

import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class AgreeToTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository
) {
    suspend operator fun invoke(termConsent: TermConsent) =
        termsRepository.agreeToTerms(termConsent)
}
