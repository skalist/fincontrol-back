package com.fincontrol.dto

/**
 * Dto for getting information about current user
 */
data class UserDto(
    val username: String,
    val firstName: String?,
    val lastName: String?,
)
