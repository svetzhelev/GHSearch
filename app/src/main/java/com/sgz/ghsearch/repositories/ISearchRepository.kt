package com.sgz.ghsearch.repositories

import com.sgz.ghsearch.models.GitRepository

interface ISearchRepository {
    fun reset()
    fun searchQuery() : String?
    fun hasMoreToLoad(): Boolean
    suspend fun searchRepositories(searchTerm: String): List<GitRepository>?
}