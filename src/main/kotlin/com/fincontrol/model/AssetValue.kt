package com.fincontrol.model

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class AssetValue(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    @Type(type = "uuid-char")
    val userId: UUID,
    @ManyToOne
    @JoinColumn(name = "asset_id")
    val asset: Asset,
    @Enumerated(EnumType.STRING)
    val event: AssetEvent,
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)
