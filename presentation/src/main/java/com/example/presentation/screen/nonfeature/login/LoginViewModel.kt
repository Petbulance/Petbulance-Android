package com.example.presentation.screen.nonfeature.login

import android.util.Log
import com.example.domain.model.nonfeature.login.UserInfo
import com.example.domain.model.nonfeature.login.LoginProviderType
import com.example.domain.usecase.nonfeature.login.RequestTokenToServerUseCase
import com.example.domain.usecase.nonfeature.login.SaveTokensUseCaseUseCase
import com.example.domain.utils.login.DomainLoginRequest
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestTokenToServerUseCase: RequestTokenToServerUseCase,
    private val saveTokensUseCase: SaveTokensUseCaseUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Init)
    val state: StateFlow<LoginState> = _state

    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow: SharedFlow<LoginEvent> = _eventFlow

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                if (intent.accessToken.isNotBlank()) {
                    launch {
                        requestTokenToServer(intent.accessToken, intent.provider)
                    }
                } else {
                    launch {
                        _eventFlow.emit(
                            LoginEvent.DataFetch.Error(
                                "로그인에 실패했습니다.",
                                "ID Token is null or empty"
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun requestTokenToServer(accessToken: String, platform: LoginProviderType) {
        _state.value = LoginState.OnProgress

        runCatching {
            val domainLoginRequest = DomainLoginRequest.builder()
                .accessToken(accessToken)
                .platform(platform)
                .build()
            requestTokenToServerUseCase(domainLoginRequest)
        }.onSuccess { userInfo ->
            saveToken(userInfo.getOrThrow())
            _eventFlow.emit(LoginEvent.Login.Success)
        }.onFailure { exception ->
            _eventFlow.emit(
                LoginEvent.Login.Error(
                    userMessage = exception.message ?: "알 수 없는 오류가 발생했습니다.",
                    exceptionMessage = exception.cause?.message
                )
            )
        }
        _state.value = LoginState.Init
    }

    private suspend fun saveToken(userInfo: UserInfo) {
        _state.value = LoginState.OnProgress
        runCatching {
            saveTokensUseCase(userInfo.accessToken, userInfo.refreshToken)
        }.onSuccess { result ->
            Log.d("siria22", "Login Success :\n $userInfo")
        }.onFailure { ex ->
            _eventFlow.emit(
                LoginEvent.DataFetch.Error(
                    userMessage = "error messages",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = LoginState.Init
    }
}
