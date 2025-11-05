package com.example.data.repository.nonfeature.terms

import android.util.Log
import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.domain.model.nonfeature.terms.TermsStatusType
import com.example.domain.repository.nonfeature.terms.TermsRepository
import java.time.Instant
import javax.inject.Inject

class MockTermsRepository @Inject constructor() : TermsRepository {

    private val mockTerms = listOf(
        Term.stub(),
        Term(
            id = "2",
            title = "개인정보 수집 및 이용 동의",
            required = true,
            summary = "개인정보 수집 및 이용에 관한 필수 약관입니다.",
            version = "1.0.0",
            lastUpdated = Instant.parse("2024-01-01T00:00:00Z")
        ),
        Term(
            id = "3",
            title = "마케팅 정보 수신 동의",
            required = false,
            summary = "이벤트 및 프로모션 알림을 받으실 수 있습니다.",
            version = "1.0.0",
            lastUpdated = Instant.parse("2024-01-01T00:00:00Z")
        )
    )

    private val mockTermDetails = listOf(
        TermDetails.stub(),
        TermDetails(
            id = "2",
            title = "개인정보 수집 및 이용 동의",
            required = true,
            version = "1.0.0",
            content = "제1조 (수집하는 개인정보의 항목) 회사는 회원가입, 원활한 고객상담, 각종 서비스의 제공을 위해 아래와 같은 최소한의 개인정보를 필수항목으로 수집하고 있습니다..."
        ),
        TermDetails(
            id = "3",
            title = "마케팅 정보 수신 동의",
            required = false,
            version = "1.0.0",
            content = "회사는 회원의 사전 동의를 받아 회사가 제공하는 서비스의 이벤트 및 혜택 등 다양한 정보를 푸시 알림, 이메일, SMS 등의 방법으로 전송할 수 있습니다..."
        )
    )

    override suspend fun getLatestTermsAgreementStatus(): Result<TermsStatusType> {
        return Result.success(TermsStatusType.TERMS_UPDATE_REQUIRED)
    }

    override suspend fun getTerms(): Result<List<Term>> {
        return Result.success(mockTerms)
    }

    override suspend fun getTermDetails(termsId: String): Result<TermDetails> {
        val detail = mockTermDetails.find { it.id == termsId }
        return if (detail != null) {
            Result.success(detail)
        } else {
            Result.failure(Exception("Term details not found for id: $termsId"))
        }
    }

    override suspend fun agreeToTerms(termConsent: TermConsent): Result<Unit> {
        Log.d("siria22", "agree to terms : $termConsent")
        return Result.success(Unit)
    }
}