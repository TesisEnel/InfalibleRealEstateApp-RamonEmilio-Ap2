package com.infaliblerealestate.presentation.util.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null,
)