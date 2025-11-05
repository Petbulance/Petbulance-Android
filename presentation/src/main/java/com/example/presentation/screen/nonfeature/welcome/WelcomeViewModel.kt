package com.example.presentation.screen.nonfeature.welcome

import androidx.lifecycle.SavedStateHandle
import com.example.domain.usecase.feature.users.RequestTemporaryNicknameUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestTemporaryNicknameUseCase: RequestTemporaryNicknameUseCase

) : BaseViewModel() {

    private val _state = MutableStateFlow<WelcomeState>(WelcomeState.Init)
    val state: StateFlow<WelcomeState> = _state

    private val _eventFlow = MutableSharedFlow<WelcomeEvent>()
    val eventFlow: SharedFlow<WelcomeEvent> = _eventFlow

    private val _tempUserName = MutableStateFlow("")
    val tempUserName: StateFlow<String> = _tempUserName

    fun onIntent(intent: WelcomeIntent) {}

    init {
        launch {
            createTempUserName()
        }
    }

    private suspend fun createTempUserName() {
        _state.value = WelcomeState.OnProgress
        runCatching {
            requestTemporaryNicknameUseCase()
        }.onSuccess { result ->
            _tempUserName.value = result.getOrThrow()
        }.onFailure { ex ->
            _eventFlow.emit(
                WelcomeEvent.DataFetch.Error(
                    userMessage = "닉네임 생성에 실패했어요.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = WelcomeState.Init
    }
}
