package com.sgz.ghsearch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitRepositoriesList(
    val items: List<GitRepository>,
    @SerialName("total_count") val totalCount: Int
)
