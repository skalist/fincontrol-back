package com.fincontrol.config.auth

import com.fincontrol.model.User
import com.fincontrol.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return UserPrincipal(user.id.toString(), user.username, user.password)
    }

    fun loadUserById(id: String): UserDetails {
        val user = userRepository.findById(UUID.fromString(id))
            .orElseThrow { throw UsernameNotFoundException("User not found with id: $id") }
        return UserPrincipal(user.id.toString(), user.username, user.password)
    }

    @Transactional
    fun save(user: User): User {
        return userRepository.save(user)
    }
}
