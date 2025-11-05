package com.example.domain.usecase.nonfeature.terms

import com.example.domain.model.nonfeature.terms.TermsStatusType
import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class CheckLatestTermsAgreementStatusUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(): Result<TermsStatusType> {
        return repository.getLatestTermsAgreementStatus()
    }
}
