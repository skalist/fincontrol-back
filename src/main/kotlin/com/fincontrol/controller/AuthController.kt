package com.fincontrol.controller

import com.fincontrol.config.auth.JwtTokenProvider
import com.fincontrol.dto.UserDto
import com.fincontrol.dto.auth.JwtAuthenticationDto
import com.fincontrol.dto.auth.LoginDto
import com.fincontrol.dto.auth.SignUpDto
import com.fincontrol.model.User
import com.fincontrol.service.CustomUserDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 * Controller for user actions, such as authentication user and creating new user
 */
@RestController
@RequestMapping("auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
) {
    /**
     * Authentication user method
     * @param dto credentials of user
     * @return authorization token
     */
    @PostMapping("login")
    fun login(@RequestBody dto: LoginDto): ResponseEntity<JwtAuthenticationDto> {
        val authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(dto.username, dto.password))
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)
        return ResponseEntity.ok(JwtAuthenticationDto(jwt))
    }

    /**
     * Create new user
     * @param dto data about new user
     * @return result of creating new user
     */
    @PostMapping("signup")
    fun signup(@RequestBody dto: SignUpDto): ResponseEntity<String> {
        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.password),
            active = false,
            firstName = dto.firstName,
            lastName = dto.lastName
        )

        customUserDetailsService.save(user)
        return ResponseEntity.ok("User registered successfully")
    }

    /**
     * Get current user info
     * @return user info
     */
    @GetMapping
    fun getUser(): UserDto = customUserDetailsService.getUser()
}
