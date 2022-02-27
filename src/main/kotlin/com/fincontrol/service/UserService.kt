package com.fincontrol.service

import com.fincontrol.dto.UserDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.User
import com.fincontrol.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val authenticationFacade: AuthenticationFacade,
    private val userRepository: UserRepository,
) {
    fun getUser(): UserDto {
        val userId = authenticationFacade.getUserId()
        val user = userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(User::class.java.simpleName, userId) }
        return user.let { UserDto(it.username, it.firstName, it.lastName) }
    }
}
