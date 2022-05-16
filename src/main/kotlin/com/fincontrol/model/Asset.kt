package com.fincontrol.model

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

/**
 * Model for asset entity
 */
@Entity
data class Asset(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    @Type(type = "uuid-char")
    val userId: UUID,
    val name: String,
    @Enumerated(EnumType.STRING)
    val type: AssetType,
    @Enumerated(EnumType.STRING)
    val currency: Currency,
    val code: String?,
    val description: String?,
    @ManyToOne
    @JoinColumn(name = "industry_id")
    val industry: Industry?,
)
