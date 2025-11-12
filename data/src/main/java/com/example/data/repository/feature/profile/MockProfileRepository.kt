package com.example.data.repository.feature.profile

import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.repository.feature.profile.ProfileRepository
import javax.inject.Inject

class MockProfileRepository @Inject constructor() : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile> {
        return Result.success(UserProfile.stub())
    }

}