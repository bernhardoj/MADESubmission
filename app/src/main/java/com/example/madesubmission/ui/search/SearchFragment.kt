package com.example.madesubmission.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
import com.example.madesubmission.core.ui.PagedGameAdapter
import com.example.madesubmission.core.ui.paging.GameLoadStateAdapter
import com.example.madesubmission.databinding.FragmentSearchBinding
import com.example.madesubmission.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()
    private var binding: FragmentSearchBinding? = null
    private var job: Job? = null
    private var searchJob: Job? = null
    private lateinit var searchAdapter: PagedGameAdapter
    private lateinit var recentSearchAdapter: RecentSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { binding ->
            initAdapter(binding)
            initViewHolder(binding)

            binding.searchGame.setOnQueryTextListener(searchListener)

            searchViewModel.queryLiveData.observe(viewLifecycleOwner) {
                search(it)
            }

            searchViewModel.recentSearchLiveData.observe(viewLifecycleOwner) {
                if (it.isEmpty()) binding.noRecentSearch.visibility = View.VISIBLE
                else {
                    binding.noRecentSearch.visibility = View.GONE
                    recentSearchAdapter.setList(it)
                }
            }

            binding.list.retryButton.setOnClickListener {
                searchAdapter.retry()
            }
        }
    }

    private fun initAdapter(binding: FragmentSearchBinding) {
        // Search result adapter
        searchAdapter = PagedGameAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.GAME_EXTRA, it)
            startActivity(intent)
        }

        searchAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    showRecentSearch(false)
                    binding.list.progressBar.visibility = View.VISIBLE
                    binding.list.retryButton.visibility = View.GONE
                    binding.list.errorTv.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.list.progressBar.visibility = View.GONE
                    if (searchAdapter.itemCount == 0) binding.list.errorTv.text =
                        context?.getString(R.string.no_games_found)
                }
                is LoadState.Error -> {
                    binding.list.errorTv.apply {
                        text = context.getString(R.string.network_error)
                        visibility = View.VISIBLE
                    }
                    binding.list.progressBar.visibility = View.GONE
                    binding.list.retryButton.visibility = View.VISIBLE
                }
            }
        }

        // Recent search adapter
        recentSearchAdapter = RecentSearchAdapter {
            binding.searchGame.setQuery(it, false)
        }
    }

    private fun initViewHolder(binding: FragmentSearchBinding) {
        // Search result RecyclerView
        binding.list.rvGames.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter.withLoadStateFooter(GameLoadStateAdapter {
                searchAdapter.retry()
            })
        }

        // Recent search RecyclerView
        binding.recentRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentSearchAdapter
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchAdapter.submitData(PagingData.empty())
            searchViewModel.searchGames(query).collectLatest {
                searchViewModel.saveRecentSearch(query)
                searchAdapter.submitData(it)
            }
        }
    }

    private fun showRecentSearch(show: Boolean) {
        binding?.let {
            it.recentRv.isVisible = show
            it.recentSearchHeading.isVisible = show
            it.list.listLayout.isVisible = !show
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            job?.cancel()
            job = lifecycleScope.launch {
                searchViewModel.queryChannel.send(newText.toString())
            }

            if (newText.toString().isEmpty()) showRecentSearch(true)

            return true
        }
    }
}