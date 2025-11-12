package com.example.domain.repository.feature.profile

import com.example.domain.model.feature.profile.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile>
}