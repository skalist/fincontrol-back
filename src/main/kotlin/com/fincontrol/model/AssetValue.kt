package com.fincontrol.model

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*

/**
 * Model for asset value entity
 */
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
    @ManyToOne
    @JoinColumn(name = "broker_account_id")
    val brokerAccount: BrokerAccount,
    @Enumerated(EnumType.STRING)
    val event: AssetEvent,
    val dateCreated: LocalDate,
    val assetsCount: Int,
    val cost: BigDecimal,
)
