package com.example.data.repository.nonfeature.terms

import com.example.data.remote.local.database.nonfeature.terms.TermDetailsDao
import com.example.data.remote.network.nonfeature.terms.TermsApi
import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.domain.model.nonfeature.terms.TermsStatusType
import com.example.domain.model.nonfeature.terms.toTermsAgreement
import com.example.domain.repository.nonfeature.terms.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termsApi: TermsApi,
    private val termDetailsDao: TermDetailsDao
) : TermsRepository {

    override suspend fun getLatestTermsAgreementStatus(): Result<TermsStatusType> {
        return termsApi.getLatestTermsAgreementStatus().map { it.status.toTermsAgreement() }
    }

    override suspend fun getTerms(): Result<List<Term>> {
        return termsApi.getTerms().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTermDetails(termsId: String): Result<TermDetails> {
        return runCatching {
            termDetailsDao.getTermDetailsById(termsId).toDomain()
        }.recoverCatching {
            val remoteDetails = termsApi.getTermDetails(termsId).getOrThrow()
            termDetailsDao.upsertTermDetails(remoteDetails.toEntity())
            remoteDetails.toDomain()
        }
    }

    override suspend fun agreeToTerms(termConsent: TermConsent): Result<Unit> {
        return termsApi.postConsents(termConsent.toRequest())
    }
}