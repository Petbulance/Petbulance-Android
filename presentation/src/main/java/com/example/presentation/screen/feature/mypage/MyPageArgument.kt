package com.example.presentation.screen.feature.mypage

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageArgument(
    val intent: (MyPageIntent) -> Unit,
    val state: MyPageState,
    val event: SharedFlow<MyPageEvent>
)

sealed class MyPageState {
    data object Init : MyPageState()
    data object OnProgress : MyPageState()
}

sealed class MyPageIntent {
    data class SomeIntentWithParams(val param: String) : MyPageIntent()
    data object SomeIntentWithoutParams : MyPageIntent()
}

sealed class MyPageEvent {
    sealed class DataFetch : MyPageEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    data object IsNotLoginUser: MyPageEvent()
}