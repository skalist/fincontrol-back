package com.fincontrol.model

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Model for operation category entity
 */
@Entity
data class OperationCategory(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    @Type(type = "uuid-char")
    val userId: UUID,
    val name: String,
)
