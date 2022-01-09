package com.sgz.ghsearch.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val message: String
)
