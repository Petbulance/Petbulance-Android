package com.example.domain.repository.nonfeature.preference

import com.example.domain.model.type.AppTheme
import com.example.domain.usecase.nonfeature.preference.PreferenceRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    suspend operator fun invoke(theme: AppTheme): Result<Unit> {
        return preferenceRepository.updateTheme(theme.name)
    }
}
