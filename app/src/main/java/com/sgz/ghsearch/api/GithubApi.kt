package com.sgz.ghsearch.api

import com.sgz.ghsearch.Config
import com.sgz.ghsearch.models.GitRepositoriesList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") searchTerm: String,
        @Query("page") page: Int = 0,
        @Query("per_page") pageSize: Int = Config.DEFAULT_PAGE_SIZE
    ): Call<GitRepositoriesList>
}