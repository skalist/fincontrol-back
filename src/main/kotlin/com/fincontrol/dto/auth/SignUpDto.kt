package com.fincontrol.dto.auth

/**
 * Dto for creating new user
 */
data class SignUpDto(
    val username: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
)
