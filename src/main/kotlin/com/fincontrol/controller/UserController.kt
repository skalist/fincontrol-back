package com.fincontrol.controller

import com.fincontrol.dto.UserDto
import com.fincontrol.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getUser(): UserDto = userService.getUser()
}
