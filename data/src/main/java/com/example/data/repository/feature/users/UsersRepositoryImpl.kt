package com.example.data.repository.feature.users

import com.example.data.remote.network.feature.users.UserApi
import com.example.data.remote.network.feature.users.model.SaveUserNicknameRequest
import com.example.domain.model.feature.users.DomainValidateNickname
import com.example.domain.repository.feature.users.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UsersRepository {

    override suspend fun saveNickname(nickname: String): Result<Unit> = runCatching {
        userApi.saveNickname(request = SaveUserNicknameRequest(nickname = nickname))
    }

    override suspend fun requestTemporaryNickname(): Result<String> = runCatching {
        userApi.requestTemporaryNickname().getOrThrow().nickname
    }

    override suspend fun validateNickname(nickname: String): Result<DomainValidateNickname> {
        return userApi.validateNickname(nickname).map { it.toDomain() }
    }
}
