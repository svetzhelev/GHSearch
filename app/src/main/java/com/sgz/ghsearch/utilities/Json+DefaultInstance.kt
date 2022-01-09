package com.sgz.ghsearch.utilities

import kotlinx.serialization.json.Json

val jsonDefaultInstance = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = false }