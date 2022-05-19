package com.fincontrol

import com.fincontrol.config.auth.UserPrincipal
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserUserSecurityContextFactory::class)
annotation class WithMockCustomUser

class WithMockCustomUserUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser?> {
    override fun createSecurityContext(annotation: WithMockCustomUser?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val password = "password"
        val principal = UserPrincipal(TestConstants.userId.toString(), "test", password, true)

        val auth = UsernamePasswordAuthenticationToken(principal, password, listOf())
        context.authentication = auth
        return context
    }
}
