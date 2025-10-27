package com.example.presentation.screen.nonfeature.terms

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class TermsArgument(
    val intent: (TermsIntent) -> Unit,
    val state: TermsState,
    val event: SharedFlow<TermsEvent>
)

sealed class TermsState {
    data object Init : TermsState()
    data object OnProgress : TermsState()
    data object Error : TermsState()
    data object Success : TermsState()
}

sealed class TermsIntent {
    data class OnTermCheckedChange(val termId: String, val isChecked: Boolean) : TermsIntent()
    data object OnAgreementButtonClicked : TermsIntent()
}

sealed class TermsEvent {
    sealed class DataFetch : TermsEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    sealed class Agreement : TermsEvent() {
        data object Success : Agreement()
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : Agreement(), ErrorEvent
    }
}