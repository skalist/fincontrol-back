package com.fincontrol.dto.auth

data class SignUpDto(
    val username: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
)
