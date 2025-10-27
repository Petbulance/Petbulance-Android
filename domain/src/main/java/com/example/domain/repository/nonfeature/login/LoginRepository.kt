package com.example.domain.repository.nonfeature.login

import com.example.domain.model.nonfeature.login.UserInfo
import com.example.domain.utils.login.DomainLoginRequest

interface LoginRepository {
    suspend fun signInWith(domainLoginRequest: DomainLoginRequest): Result<UserInfo>
}