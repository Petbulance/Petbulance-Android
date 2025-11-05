package com.example.presentation.utils

import android.content.Context
import com.example.presentation.BuildConfig
import com.example.presentation.R
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NidOAuth
import jakarta.inject.Inject

interface SdkInitializer {
    fun initialize(context: Context)
}

class SdkInitializerImpl @Inject constructor() : SdkInitializer {
    override fun initialize(context: Context) {

        NidOAuth.initialize(
            context = context,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            clientName = context.getString(R.string.naver_client_name)
        )

        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
