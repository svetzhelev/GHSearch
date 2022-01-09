package com.sgz.ghsearch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgz.ghsearch.Config
import com.sgz.ghsearch.models.GitRepository
import com.sgz.ghsearch.repositories.ISearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubSearchViewModel @Inject constructor(private val repository: ISearchRepository) :
    ViewModel() {

    private val _error: MutableLiveData<String?> = MutableLiveData(null)
    val error: LiveData<String?> = _error

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _repositories: MutableLiveData<List<GitRepository>> = MutableLiveData(emptyList())
    val repositories: LiveData<List<GitRepository>> = _repositories
    val hasMoreToLoad : Boolean
        get() = repository.hasMoreToLoad()

    fun initSearch(searchTerm: String) {
        //  Query term has changed, reset the search.
        repository.reset()
        _repositories.postValue(emptyList())
        search(searchTerm)
    }

    fun search(searchTerm: String? = repository.searchQuery()) {
        _error.postValue(null)
        _loading.postValue(true)

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val badge = searchTerm?.let { query -> repository.searchRepositories(query) }

                //  Merge current results list with the new badge
                val mergedList = _repositories.value?.toMutableList()?.also { currentItems ->
                    badge?.let { newItems -> currentItems.addAll(newItems) }
                }

                _repositories.postValue(mergedList)
            } catch (e: Throwable) {
                _error.postValue(e.localizedMessage)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}