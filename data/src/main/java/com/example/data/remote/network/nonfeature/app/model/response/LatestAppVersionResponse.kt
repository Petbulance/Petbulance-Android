package com.example.data.remote.network.nonfeature.app.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LatestAppVersionResponse(
    val version: String
)
