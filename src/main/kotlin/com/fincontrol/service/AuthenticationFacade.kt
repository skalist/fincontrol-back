package com.fincontrol.service

import com.fincontrol.config.auth.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

/**
 * Service for getting info about current user
 */
@Component
class AuthenticationFacade {
    /**
     * Getting current user identifier
     * @return identifier
     */
    fun getUserId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
            .let { UUID.fromString(it) }
    }
}
