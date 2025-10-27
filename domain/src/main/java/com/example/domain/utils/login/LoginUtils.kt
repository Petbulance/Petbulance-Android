package com.example.domain.utils.login

import com.example.domain.model.type.LoginProvider

data class DomainLoginRequest(
    val accessToken: String,
    val platform: String
) {
    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var accessToken: String = ""
        private var platform: String = ""

        fun accessToken(accessToken: String) = apply { this.accessToken = accessToken }
        fun platform(provider: LoginProvider) = apply { this.platform = provider.name }

        fun build() = DomainLoginRequest(
            accessToken = accessToken,
            platform = platform
        )
    }
}
