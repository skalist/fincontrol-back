package com.fincontrol.repository

import com.fincontrol.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
    fun findByUsernameAndActiveIsTrue(username: String): User?
}

