package com.example.domain.usecase.nonfeature.terms

import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class GetTermDetailsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke(termsId: String)
    = termsRepository.getTermDetails(termsId)
}
