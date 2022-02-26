package com.fincontrol.controller

import com.fincontrol.config.auth.JwtTokenProvider
import com.fincontrol.config.auth.UserDetailsServiceImpl
import com.fincontrol.dto.auth.JwtAuthenticationDto
import com.fincontrol.dto.auth.LoginDto
import com.fincontrol.dto.auth.SignUpDto
import com.fincontrol.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsServiceImpl
) {
    @PostMapping("login")
    fun login(@RequestBody dto: LoginDto): ResponseEntity<JwtAuthenticationDto> {
        val authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(dto.username, dto.password))
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)
        return ResponseEntity.ok(JwtAuthenticationDto(jwt))
    }

    @PostMapping("signup")
    fun signup(@RequestBody dto: SignUpDto): ResponseEntity<String> {
        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.password),
            active = false,
            firstName = dto.firstName,
            lastName = dto.lastName
        )

        userDetailsService.save(user)
        return ResponseEntity.ok("User registered successfully")
    }
}
