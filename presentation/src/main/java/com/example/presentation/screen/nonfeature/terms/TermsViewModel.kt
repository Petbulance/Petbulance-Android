package com.example.presentation.screen.nonfeature.terms

import android.util.Log
import com.example.domain.model.nonfeature.terms.Consent
import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermConsent
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.domain.usecase.nonfeature.terms.AgreeToTermsUseCase
import com.example.domain.usecase.nonfeature.terms.GetTermDetailsUseCase
import com.example.domain.usecase.nonfeature.terms.GetTermsUseCase
import com.example.domain.utils.zip
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase,
    private val getTermDetailsUseCase: GetTermDetailsUseCase,
    private val agreeToTermsUseCase: AgreeToTermsUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<TermsState>(TermsState.Init)
    val state: StateFlow<TermsState> = _state

    private val _eventFlow = MutableSharedFlow<TermsEvent>()
    val eventFlow: SharedFlow<TermsEvent> = _eventFlow

    private val _termList = MutableStateFlow<List<Term>>(emptyList())
    val termList: StateFlow<List<Term>> = _termList

    private val _termDetails = MutableStateFlow<List<TermDetails>>(emptyList())
    val termDetails: StateFlow<List<TermDetails>> = _termDetails

    private val _consents = MutableStateFlow<List<Consent>>(emptyList())

    private val _isAllRequiredTermsAgreed = MutableStateFlow(false)
    val isAllRequiredTermsAgreed: StateFlow<Boolean> = _isAllRequiredTermsAgreed

    fun onIntent(intent: TermsIntent) {
        when (intent) {
            is TermsIntent.OnTermCheckedChange -> {
                launch {
                    onConsentChanged(intent.termId, intent.isChecked, Instant.now())
                }
            }

            is TermsIntent.OnAgreementButtonClicked -> {
                launch {
                    agreeToTerms()
                }
            }
        }
    }

    init {
        launch {
            getTerms()
        }
    }

    private suspend fun getTerms() {
        _state.value = TermsState.OnProgress
        runCatching {
            getTermsUseCase()
        }.onSuccess { result ->
            _termList.value = result.getOrThrow()
            zip(
                { initConsents() },
                { getTermDetails() }
            )
        }.onFailure { ex ->
            _eventFlow.emit(
                TermsEvent.DataFetch.Error(
                    userMessage = "약관 목록을 불러오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = TermsState.Init
    }

    private suspend fun getTermDetails() {
        _state.value = TermsState.OnProgress
        runCatching {
            coroutineScope {
                val deferredDetails = _termList.value.map { term ->
                    async { getTermDetailsUseCase(term.id) }
                }
                val results = deferredDetails.awaitAll()
                _termDetails.value = results.map { it.getOrThrow() }
            }
        }.onFailure { ex ->
            _eventFlow.emit(
                TermsEvent.DataFetch.Error(
                    userMessage = "약관 상세 정보를 불러오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = TermsState.Init
    }

    private fun initConsents() {
        _consents.value = _termList.value.map { term ->
            Consent(
                termId = term.id,
                agreed = false,
                updatedAt = null
            )
        }
    }

    private suspend fun onConsentChanged(termId: String, checked: Boolean, updatedAt: Instant?) {
        _state.value = TermsState.OnProgress
        runCatching {
            _consents.value = _consents.value.map { consent ->
                if (consent.termId == termId) {
                    consent.copy(
                        agreed = checked,
                        updatedAt = updatedAt
                    )
                } else consent
            }
            validateAllRequiredConsents()
        }.onFailure { ex ->
            _eventFlow.emit(
                TermsEvent.DataFetch.Error(
                    userMessage = "Something went wrong",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = TermsState.Init
    }

    private suspend fun agreeToTerms() {
        _state.value = TermsState.OnProgress
        runCatching {
            validateAllRequiredConsents()
            agreeToTermsUseCase(TermConsent(_consents.value))
        }.onSuccess { result ->
            _eventFlow.emit(TermsEvent.Agreement.Success)
        }.onFailure { ex ->
            _eventFlow.emit(
                TermsEvent.Agreement.Error(
                    userMessage = "필수 약관이 모두 체크되지 않았습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = TermsState.Init
    }

    private fun validateAllRequiredConsents() {
        val requiredIds = _termList.value.filter { it.required }.map { it.id }
        val consentMap = _consents.value.associateBy { it.termId }
        val notAgreed = requiredIds.any { id -> consentMap[id]?.agreed != true }
        if (notAgreed) {
            Log.d("siria22", "전부 동의되지 않음 어쩌고")
            _isAllRequiredTermsAgreed.value = false
            return
        }
        _isAllRequiredTermsAgreed.value = true
    }

}
