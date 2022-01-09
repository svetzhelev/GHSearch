package com.sgz.ghsearch.repositories

import com.sgz.ghsearch.Config
import com.sgz.ghsearch.api.GithubApi
import com.sgz.ghsearch.api.wrappers.ApiResponse
import com.sgz.ghsearch.models.GitRepository
import kotlin.math.ceil

class GithubRepository(
    private val githubApi: GithubApi,
    private val state: State
) : ISearchRepository {

    interface State {
        companion object

        var query: String?
        var page: Int
        var totalResultsCount: Int
    }

    override suspend fun searchRepositories(searchTerm: String): List<GitRepository>? {
        state.query = searchTerm
        val response = ApiResponse(githubApi.searchRepositories(searchTerm, ++state.page))

        response.data?.let {
            state.totalResultsCount = it.totalCount
        }

        return response.data?.items
    }

    override fun reset() {
        state.totalResultsCount = 0
        state.page = 0
        state.query = null
    }

    override fun searchQuery() = state.query

    override fun hasMoreToLoad(): Boolean =
        ceil(state.totalResultsCount / Config.DEFAULT_PAGE_SIZE.toDouble()) > state.page
}