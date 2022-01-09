package com.sgz.ghsearch.utilities

import com.sgz.ghsearch.repositories.GithubRepository

fun GithubRepository.State.Companion.inMemory() : GithubRepository.State = object: GithubRepository.State {
    override var totalResultsCount: Int = 0
    override var query: String? = null
    override var page: Int = 0
}