package com.fincontrol.service

import com.fincontrol.config.auth.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationFacade {
    fun getUserId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
            .let { UUID.fromString(it) }
    }
}
