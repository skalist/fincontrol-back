package com.fincontrol.model

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class BankAccount(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    val name: String,
)
