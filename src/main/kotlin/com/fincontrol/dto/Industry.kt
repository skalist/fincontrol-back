package com.fincontrol.dto

import java.util.UUID

data class IndustryListDto(
    val id: UUID,
    val name: String,
)

data class IndustryUpsertDto(
    val id: UUID?,
    val name: String,
)
