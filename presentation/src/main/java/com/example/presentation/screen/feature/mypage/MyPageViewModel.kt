package com.example.presentation.screen.feature.mypage

import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.usecase.feature.profile.GetUserProfileUseCase
import com.example.domain.usecase.nonfeature.app.GetAppVersionsUseCase
import com.example.domain.utils.zip
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getAppVersionsUseCase: GetAppVersionsUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<MyPageState>(MyPageState.Init)
    val state: StateFlow<MyPageState> = _state

    private val _eventFlow = MutableSharedFlow<MyPageEvent>()
    val eventFlow: SharedFlow<MyPageEvent> = _eventFlow

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _currentAppVersion = MutableStateFlow<String>("")
    val currentAppVersion: StateFlow<String> = _currentAppVersion

    private val _latestAppVersion = MutableStateFlow<String>("")
    val latestAppVersion: StateFlow<String> = _latestAppVersion

    fun onIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is MyPageIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }
        }
    }

    init {
        launch {
            zip(
                { getUserProfile() },
                { getAppVersions() }
            )
        }
    }

    private suspend fun getUserProfile() {
        _state.value = MyPageState.OnProgress
        runCatching {
            getUserProfileUseCase()
        }.onSuccess { result ->
            _userProfile.value = result.getOrThrow()
        }.onFailure { ex ->
            _eventFlow.emit(
                MyPageEvent.DataFetch.Error(
                    userMessage = "사용자 정보를 불러오는데 실패했어요.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = MyPageState.Init
    }

    private suspend fun getAppVersions() {
        _state.value = MyPageState.OnProgress
        runCatching {
            getAppVersionsUseCase()
        }.onSuccess { result ->
            val (currentVersion, latestVersion) = result.getOrThrow()
            _currentAppVersion.value = currentVersion
            _latestAppVersion.value = latestVersion
        }.onFailure { ex ->
            _eventFlow.emit(
                MyPageEvent.DataFetch.Error(
                    userMessage = "최신 앱 버전을 불러오는데 실패했어요.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = MyPageState.Init
    }
}
