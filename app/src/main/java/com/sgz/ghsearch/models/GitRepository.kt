package com.sgz.ghsearch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GitRepository(
    val name: String,
    val owner: Owner,
    @SerialName("html_url")val url: String,
    val description: String? = null
) {

    @Serializable
    class Owner(
        val login: String,
        @SerialName("avatar_url")val avatarUrl: String
    )
}
