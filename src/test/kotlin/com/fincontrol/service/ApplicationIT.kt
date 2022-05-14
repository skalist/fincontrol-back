package com.fincontrol.service

import com.fincontrol.FincontrolBackApplication
import com.fincontrol.config.auth.UserPrincipal
import com.fincontrol.controller.BankAccountController
import com.fincontrol.dto.BankAccountUpsertDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*


@Testcontainers
@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [FincontrolBackApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class ApplicationIT {

    @Autowired
    private lateinit var bankAccountController: BankAccountController

    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("testdb")
            withUsername("duke")
            withPassword("s3crEt")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }
    }

    @Test
    @WithMockCustomUser
    fun `simple test`() {
        val create = bankAccountController.create(BankAccountUpsertDto(id = null, name = "test"))

        assertThat(create).isEqualTo(UUID.randomUUID())
    }
}

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserUserSecurityContextFactory::class)
annotation class WithMockCustomUser

class WithMockCustomUserUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser?> {
    override fun createSecurityContext(annotation: WithMockCustomUser?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val password = "password"
        val principal = UserPrincipal(UUID.randomUUID().toString(), "test", password)

        val auth = UsernamePasswordAuthenticationToken(principal, password, listOf())
        context.authentication = auth
        return context
    }
}
