package com.example.presentation.screen.nonfeature.welcome

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class WelcomeArgument(
    val intent: (WelcomeIntent) -> Unit,
    val state: WelcomeState,
    val event: SharedFlow<WelcomeEvent>
)

sealed class WelcomeState {
    data object Init : WelcomeState()
    data object OnProgress : WelcomeState()
}

sealed class WelcomeIntent

sealed class WelcomeEvent {
    sealed class DataFetch : WelcomeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}