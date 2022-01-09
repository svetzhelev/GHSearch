package com.sgz.ghsearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgz.ghsearch.databinding.SearchEntryItemBinding
import com.sgz.ghsearch.models.GitRepository
import com.sgz.ghsearch.ui.viewholders.SearchEntryViewHolder

class GHSearchAdapter(private val itemClickListener: RepositoryTapListener) :
    RecyclerView.Adapter<SearchEntryViewHolder>() {
    private var items: List<GitRepository> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchEntryViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: SearchEntryItemBinding = SearchEntryItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )

        return SearchEntryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchEntryViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setData(repos: List<GitRepository>) {
        val oldItemsLastIdx = items.size - 1

        items = repos

        notifyItemRangeChanged(oldItemsLastIdx, repos.size)
    }
}

interface RepositoryTapListener {
    fun onRepositoryTapped(repo: GitRepository)
}