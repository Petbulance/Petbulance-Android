package com.example.domain.usecase.feature.users

import com.example.domain.model.feature.users.DomainValidateNickname
import com.example.domain.repository.feature.users.UsersRepository
import javax.inject.Inject

class ValidateNicknameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(nickname: String): Result<DomainValidateNickname> {
        return usersRepository.validateNickname(nickname)
    }
}
