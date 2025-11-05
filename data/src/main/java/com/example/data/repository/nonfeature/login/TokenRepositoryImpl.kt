package com.example.data.repository.nonfeature.login

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.example.data.common.di.security.AppKeyAlias
import com.example.data.common.di.security.AppKeyProvider
import com.example.data.common.di.security.EncryptedData
import com.example.domain.repository.nonfeature.login.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val appKeyProvider: AppKeyProvider,
    private val prefs: SharedPreferences
) : TokenRepository {

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        appKeyProvider.encryptData(AppKeyAlias.ACCESS_TOKEN, accessToken)?.let { encrypted ->
            prefs.edit {
                putString(
                    KEY_ACCESS_TOKEN,
                    Base64.encodeToString(encrypted.encryptedBytes, Base64.NO_WRAP)
                )
                    .putString(
                        KEY_ACCESS_TOKEN_IV,
                        Base64.encodeToString(encrypted.iv, Base64.NO_WRAP)
                    )
            }
        }

        appKeyProvider.encryptData(AppKeyAlias.REFRESH_TOKEN, refreshToken)?.let { encrypted ->
            prefs.edit {
                putString(
                    KEY_REFRESH_TOKEN,
                    Base64.encodeToString(encrypted.encryptedBytes, Base64.NO_WRAP)
                )
                    .putString(
                        KEY_REFRESH_TOKEN_IV,
                        Base64.encodeToString(encrypted.iv, Base64.NO_WRAP)
                    )
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        val encryptedString = prefs.getString(KEY_ACCESS_TOKEN, null) ?: return null
        val ivString = prefs.getString(KEY_ACCESS_TOKEN_IV, null) ?: return null

        val encryptedData = EncryptedData(
            encryptedBytes = Base64.decode(encryptedString, Base64.NO_WRAP),
            iv = Base64.decode(ivString, Base64.NO_WRAP)
        )

        return appKeyProvider.decryptData(AppKeyAlias.ACCESS_TOKEN, encryptedData)
    }

    override suspend fun getRefreshToken(): String? {
        val encryptedString = prefs.getString(KEY_REFRESH_TOKEN, null) ?: return null
        val ivString = prefs.getString(KEY_REFRESH_TOKEN_IV, null) ?: return null

        val encryptedData = EncryptedData(
            encryptedBytes = Base64.decode(encryptedString, Base64.NO_WRAP),
            iv = Base64.decode(ivString, Base64.NO_WRAP)
        )

        return appKeyProvider.decryptData(AppKeyAlias.REFRESH_TOKEN, encryptedData)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "encrypted_access_token"
        private const val KEY_ACCESS_TOKEN_IV = "encrypted_access_token_iv"
        private const val KEY_REFRESH_TOKEN = "encrypted_refresh_token"
        private const val KEY_REFRESH_TOKEN_IV = "encrypted_refresh_token_iv"
    }
}