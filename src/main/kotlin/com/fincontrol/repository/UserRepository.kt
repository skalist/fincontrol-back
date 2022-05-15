package com.fincontrol.repository

import com.fincontrol.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?
}

