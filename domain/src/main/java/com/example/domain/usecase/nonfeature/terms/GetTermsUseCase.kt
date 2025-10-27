package com.example.domain.usecase.nonfeature.terms

import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class GetTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository
) {
    suspend operator fun invoke() = termsRepository.getTerms()
}
