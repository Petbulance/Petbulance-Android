package com.example.presentation.screen.nonfeature.splash

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class SplashArgument(
    val intent: (SplashIntent) -> Unit,
    val state: SplashState,
    val event: SharedFlow<SplashEvent>
)

sealed class SplashState {
    data object Init : SplashState()
    data object OnProgress : SplashState()
}

sealed class SplashIntent {
    data class SomeIntentWithParams(val param: String) : SplashIntent()
    data object SomeIntentWithoutParams : SplashIntent()
}

sealed class SplashEvent {
    sealed class DataFetch : SplashEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    sealed class Navigate : SplashEvent() {
        data object ToLogin : Navigate()
        data object ToHome : Navigate()
        data object ToTermsAgreement : Navigate()
    }
}