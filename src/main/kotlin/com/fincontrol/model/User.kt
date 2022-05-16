package com.fincontrol.model

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * Model for user entity
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val active: Boolean,
)
