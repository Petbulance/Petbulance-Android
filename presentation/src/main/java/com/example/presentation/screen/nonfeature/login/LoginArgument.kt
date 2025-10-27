package com.example.presentation.screen.nonfeature.login

import com.example.domain.model.type.LoginProvider
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class LoginArgument(
    val intent: (LoginIntent) -> Unit,
    val state: LoginState,
    val event: SharedFlow<LoginEvent>
)

sealed class LoginState {
    data object Init : LoginState()
    data object OnProgress : LoginState()
}

sealed class LoginIntent {
    data class Login(val accessToken: String, val provider: LoginProvider) : LoginIntent()
}

sealed class LoginEvent {
    sealed class DataFetch : LoginEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    sealed class Login : LoginEvent() {
        data object Success : Login()
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : Login(), ErrorEvent
    }
}