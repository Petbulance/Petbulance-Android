package com.example.data.repository.nonfeature.login

import com.example.data.remote.network.nonfeature.users.LoginApi
import com.example.data.remote.network.nonfeature.users.model.request.toData
import com.example.domain.model.nonfeature.login.UserInfo
import com.example.domain.repository.nonfeature.login.LoginRepository
import com.example.domain.utils.login.DomainLoginRequest
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi
) : LoginRepository {

    override suspend fun signInWith(domainLoginRequest: DomainLoginRequest): Result<UserInfo> {
        val request = domainLoginRequest.toData()

        return loginApi.signIn(request).map { response ->
            UserInfo(
                isNewUser = response.isNewUser,
                signUpToken = response.signUpToken,
                firebaseCustomToken = response.firebaseCustomToken,
                accessToken = response.accessToken ?: "",
                refreshToken = response.refreshToken ?: ""
            )
        }
    }
}