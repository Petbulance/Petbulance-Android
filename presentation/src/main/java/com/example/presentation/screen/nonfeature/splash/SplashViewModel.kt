package com.example.presentation.screen.nonfeature.splash

import android.util.Log
import com.example.domain.model.nonfeature.terms.TermsStatusType
import com.example.domain.usecase.nonfeature.terms.CheckLatestTermsAgreementStatusUseCase
import com.example.presentation.utils.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkLatestTermsAgreementStatusUseCase: CheckLatestTermsAgreementStatusUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Init)
    val state: StateFlow<SplashState> = _state

    private val _eventFlow = MutableSharedFlow<SplashEvent>(replay = 1)
    val eventFlow: SharedFlow<SplashEvent> = _eventFlow

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is SplashIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }
        }
    }

    init {
        launch {
            checkUserState()
        }
    }

    private suspend fun checkUserState() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        Log.d("siria22", "firebaseUser = $user")
        if (user == null) {
            Log.d("siria22", "user not detected -> Navigate to login page")
            _eventFlow.emit(SplashEvent.Navigate.ToLogin)
        } else {
            Log.d("siria22", "user detected -> Check latest terms agreement status")
            checkLatestTermsAgreementStatus()
        }
    }

    private suspend fun checkLatestTermsAgreementStatus() {
        _state.value = SplashState.OnProgress
        runCatching {
            Log.d("siria22", "check latest terms agreement status")
            checkLatestTermsAgreementStatusUseCase()
        }.onSuccess { result ->
            val state = result.getOrThrow()
            when (state) {
                TermsStatusType.ACTIVE -> {
                    _eventFlow.emit(SplashEvent.Navigate.ToHome)
                }

                TermsStatusType.TERMS_UPDATE_REQUIRED -> {
                    _eventFlow.emit(SplashEvent.Navigate.ToTermsAgreement)
                    /* TODO : 특정 flag를 set 하는 방식으로 수정 */
                }
            }
        }.onFailure { ex ->
            _eventFlow.emit(
                SplashEvent.DataFetch.Error(
                    userMessage = "error messages",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = SplashState.Init
    }

}
