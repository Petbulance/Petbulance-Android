package com.example.domain.usecase.feature.users

import com.example.domain.repository.feature.users.UsersRepository
import javax.inject.Inject

class SaveNicknameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> {
        return usersRepository.saveNickname(nickname)
    }
}
