package com.sgz.ghsearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.sgz.ghsearch.databinding.ActivityMainBinding
import com.sgz.ghsearch.models.GitRepository
import com.sgz.ghsearch.ui.adapters.GHSearchAdapter
import com.sgz.ghsearch.ui.adapters.RepositoryTapListener
import com.sgz.ghsearch.viewmodels.GithubSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RepositoryTapListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var skeletonMask: Skeleton
    private lateinit var searchViewAdapter: GHSearchAdapter
    private var lastVisibleItemIdx: Int = -1

    private val viewModel: GithubSearchViewModel by viewModels()

    private val loadingObserver by lazy {
        viewModel.loading.observe(this) { isLoading ->

            if(isLoading) {
                skeletonMask.showSkeleton()
                return@observe
            }

            skeletonMask.showOriginal()
        }
    }

    private val repositoriesObserver by lazy {
        viewModel.repositories.observe(this) { repos ->
            repos?.let {
                binding.searchRecyclerView.scrollToPosition(lastVisibleItemIdx - 1)
                searchViewAdapter.setData(repos)
            }
        }
    }

    private val errorObserver by lazy {
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Setup RecyclerView
        searchViewAdapter = GHSearchAdapter(this)
        binding.searchRecyclerView.apply {
            adapter = searchViewAdapter
            skeletonMask = applySkeleton(R.layout.search_entry_item, Config.DEFAULT_PAGE_SIZE)

            //  Add divider for between each item
            addItemDecoration(
                DividerItemDecoration(
                    baseContext,
                    (binding.searchRecyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0 && viewModel.loading.value == false) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                        if (lastVisibleItemPosition >= layoutManager.itemCount - 1 &&
                            !skeletonMask.isSkeleton() && viewModel.hasMoreToLoad) {

                            lastVisibleItemIdx = lastVisibleItemPosition
                            viewModel.search()
                        }
                    }
                }
            })
        }

        handleIntent(intent)
    }

    override fun onStart() {
        super.onStart()

        //  Initialize observers
        loadingObserver
        repositoriesObserver
        errorObserver
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_search_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            maxWidth = Integer.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            query?.let {
                viewModel.initSearch(it)
            }
        }
    }

    override fun onRepositoryTapped(repo: GitRepository) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(repo.url)

        ContextCompat.startActivity(this, browserIntent, null)
    }
}