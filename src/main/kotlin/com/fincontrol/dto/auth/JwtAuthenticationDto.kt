package com.fincontrol.dto.auth

/**
 * Authorization token
 */
data class JwtAuthenticationDto(
    val accessToken: String,
    val tokenType: String = "Bearer",
)
