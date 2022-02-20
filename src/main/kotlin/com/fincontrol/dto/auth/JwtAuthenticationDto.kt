package com.fincontrol.dto.auth

data class JwtAuthenticationDto(
    val accessToken: String,
    val tokenType: String = "Bearer",
)
