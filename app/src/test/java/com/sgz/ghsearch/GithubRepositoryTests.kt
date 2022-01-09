package com.sgz.ghsearch

import com.sgz.ghsearch.api.mocks.mockedGithubApi
import com.sgz.ghsearch.repositories.GithubRepository
import com.sgz.ghsearch.utilities.inMemory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GithubRepositoryTests {

    @ExperimentalSerializationApi
    @Test
    fun test_search() = runBlocking {
        val state = GithubRepository.State.inMemory()
        val searchRepository = GithubRepository(mockedGithubApi, state)
        val repos = searchRepository.searchRepositories("Mock")

        assert(repos?.size == Config.DEFAULT_PAGE_SIZE)
        assert(state.page == 1)
        assert(state.totalResultsCount == 11)
        assert(state.query == "Mock")
        assert(searchRepository.hasMoreToLoad() == true)

        searchRepository.searchRepositories("Mock")

        assert(state.page == 2)
        assert(searchRepository.hasMoreToLoad() == false)

        searchRepository.reset()

        assert(state.page == 0)
        assert(state.totalResultsCount == 0)
        assert(state.query == null)
        assert(searchRepository.hasMoreToLoad() == false)
    }
}