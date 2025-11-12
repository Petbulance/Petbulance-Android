package com.example.domain.usecase.feature.profile

import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.repository.feature.profile.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getUserProfile()
    }
}
