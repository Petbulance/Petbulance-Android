package com.example.domain.usecase.feature.users

import com.example.domain.repository.feature.users.UsersRepository
import javax.inject.Inject

class RequestTemporaryNicknameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(): Result<String> {
        return usersRepository.requestTemporaryNickname()
    }
}
