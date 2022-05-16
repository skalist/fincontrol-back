package com.fincontrol.dto

/**
 * Dto for autocomplete field
 */
data class AutocompleteOption<T>(
    val value: T,
    val label: String
)
