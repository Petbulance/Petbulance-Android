package com.example.domain.usecase.nonfeature.login

import com.example.domain.model.nonfeature.login.UserInfo
import com.example.domain.repository.nonfeature.login.LoginRepository
import com.example.domain.utils.login.DomainLoginRequest
import javax.inject.Inject

class RequestTokenToServerUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(domainLoginRequest: DomainLoginRequest): Result<UserInfo> {
        return repository.signInWith(domainLoginRequest)
    }
}
