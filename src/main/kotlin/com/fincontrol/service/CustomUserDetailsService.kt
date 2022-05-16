package com.fincontrol.service

import com.fincontrol.config.auth.UserPrincipal
import com.fincontrol.dto.UserDto
import com.fincontrol.exception.EntityNotFoundException
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
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val authenticationFacade: AuthenticationFacade,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameAndActiveIsTrue(username)
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

    fun getUser(): UserDto {
        val userId = authenticationFacade.getUserId()
        val user = userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(User::class.java.simpleName, userId) }
        return user.let { UserDto(it.username, it.firstName, it.lastName) }
    }
}
