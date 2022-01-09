package com.sgz.ghsearch.di

import com.sgz.ghsearch.api.GithubApi
import com.sgz.ghsearch.repositories.GithubRepository
import com.sgz.ghsearch.repositories.ISearchRepository
import com.sgz.ghsearch.utilities.inMemory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providesGitHubRepository(githubApi: GithubApi): ISearchRepository =
        GithubRepository(githubApi, GithubRepository.State.inMemory())
}

