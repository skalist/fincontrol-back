package com.fincontrol

import com.fincontrol.controller.*
import com.fincontrol.repository.BankAccountRepository
import com.fincontrol.repository.BankOperationRepository
import com.fincontrol.repository.OperationCategoryRepository
import org.junit.jupiter.api.AfterEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [FincontrolBackApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@WithMockCustomUser
class ApplicationIT {
    @Autowired
    protected lateinit var bankAccountController: BankAccountController
    @Autowired
    protected lateinit var operationCategoryController: OperationCategoryController
    @Autowired
    protected lateinit var bankOperationController: BankOperationController
    @Autowired
    protected lateinit var bankOperationStatisticController: BankOperationStatisticController
    @Autowired
    protected lateinit var bankAccountRepository: BankAccountRepository
    @Autowired
    protected lateinit var operationCategoryRepository: OperationCategoryRepository
    @Autowired
    protected lateinit var bankOperationRepository: BankOperationRepository
    @Autowired
    protected lateinit var investmentCalculatorController: InvestmentCalculatorController

    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:14").apply {
            withDatabaseName("test_db")
            withUsername("test_user")
            withPassword("test_password")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }

    @AfterEach
    fun clear() {
        bankOperationRepository.deleteAll()
        bankAccountRepository.deleteAll()
        operationCategoryRepository.deleteAll()
    }
}
