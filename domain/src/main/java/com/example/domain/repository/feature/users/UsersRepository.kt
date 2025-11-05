package com.example.domain.repository.feature.users

import com.example.domain.model.feature.users.DomainValidateNickname

interface UsersRepository {
    suspend fun saveNickname(nickname: String): Result<Unit>
    suspend fun requestTemporaryNickname(): Result<String>
    suspend fun validateNickname(nickname: String): Result<DomainValidateNickname>
}