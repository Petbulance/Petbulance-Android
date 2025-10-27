package com.example.data.repository.nonfeature.terms

import com.example.data.remote.network.nonfeature.terms.TermsApi
import com.example.domain.model.type.TermsAgreement
import com.example.domain.model.type.toTermsAgreement
import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val api: TermsApi
) : TermsRepository {

    override suspend fun getLatestTermsAgreementStatus(): Result<TermsAgreement> {
        return api.getLatestTermsAgreementStatus().map { it.status.toTermsAgreement() }
    }

}