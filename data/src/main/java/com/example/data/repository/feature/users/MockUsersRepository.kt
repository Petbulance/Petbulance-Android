package com.example.data.repository.feature.users

import com.example.domain.model.feature.users.DomainValidateNickname
import com.example.domain.repository.feature.users.UsersRepository
import javax.inject.Inject

class MockUsersRepository @Inject constructor() : UsersRepository {

    override suspend fun saveNickname(nickname: String): Result<Unit> = runCatching {
        Result.success(Unit)
    }

    override suspend fun requestTemporaryNickname(): Result<String> = runCatching {
        "따뜻한햄스터07"
    }

    override suspend fun validateNickname(nickname: String): Result<DomainValidateNickname> {
        return Result.success(
            DomainValidateNickname(
                available = true,
                reason = null
            )
        )
    }
}
