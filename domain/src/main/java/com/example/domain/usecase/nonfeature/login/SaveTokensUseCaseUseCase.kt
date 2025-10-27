package com.example.domain.usecase.nonfeature.login

import com.example.domain.repository.nonfeature.login.TokenRepository
import javax.inject.Inject

class SaveTokensUseCaseUseCase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        repository.saveTokens(accessToken, refreshToken)
    }
}
