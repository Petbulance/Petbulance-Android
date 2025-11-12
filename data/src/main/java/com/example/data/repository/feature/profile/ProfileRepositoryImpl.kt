package com.example.data.repository.feature.profile

import com.example.data.remote.network.feature.users.UserApi
import com.example.domain.model.feature.profile.UserProfile
import com.example.domain.repository.feature.profile.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = runCatching {
        userApi.getUserProfile().getOrThrow().toDomain()
    }
}
