package com.fincontrol.dto

import java.util.UUID

data class BrokerAccountListDto(
    val id: UUID,
    val name: String,
)

data class BrokerAccountUpsertDto(
    val id: UUID?,
    val name: String,
)
