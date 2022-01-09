package com.sgz.ghsearch.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.sgz.ghsearch.databinding.SearchEntryItemBinding
import com.sgz.ghsearch.models.GitRepository
import com.sgz.ghsearch.ui.adapters.RepositoryTapListener

class SearchEntryViewHolder(private val itemBinding: SearchEntryItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(entry: GitRepository, tapListener: RepositoryTapListener) {
        itemBinding.repository = entry
        itemBinding.root.setOnClickListener { tapListener.onRepositoryTapped(entry) }
    }
}