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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
import com.example.madesubmission.core.domain.model.RecentSearch
import com.example.madesubmission.core.ui.PagedGameAdapter
import com.example.madesubmission.core.ui.paging.GameLoadStateAdapter
import com.example.madesubmission.databinding.FragmentSearchBinding
import com.example.madesubmission.ui.detail.DetailActivity
import com.example.madesubmission.util.KeyboardUtil
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
    private var isRecentSearchEmpty = true
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

            binding.searchGame.setOnQueryTextFocusChangeListener { view, b ->
                if (!b) KeyboardUtil.hideKeyboard(view)
            }

            binding.searchGame.setOnQueryTextListener(searchListener)

            searchViewModel.queryLiveData.observe(viewLifecycleOwner) {
                search(it)
            }

            searchViewModel.recentSearchLiveData.observe(viewLifecycleOwner) {
                recentSearchAdapter.setList(it)
                isRecentSearchEmpty = it.isEmpty()
                if (searchViewModel.queryChannel.value.isEmpty()) {
                    binding.noRecentSearch.isVisible = isRecentSearchEmpty
                    binding.recentRv.isVisible = !isRecentSearchEmpty
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
        searchAdapter.addLoadStateListener(loadStateListener)

        // Recent search adapter
        recentSearchAdapter = RecentSearchAdapter(object : RecentSearchListener {
            override fun onClick(query: String) = binding.searchGame.setQuery(query, false)
            override fun onLongClick(recentSearch: RecentSearch) =
                searchViewModel.deleteRecentSearch(recentSearch)
        })
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
            it.recentRv.isVisible = !isRecentSearchEmpty && show
            it.noRecentSearch.isVisible = isRecentSearchEmpty && show
            it.recentSearchHeading.isVisible = show
            it.list.listLayout.isVisible = !show
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        searchAdapter.removeLoadStateListener(loadStateListener)
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

    private val loadStateListener = { loadState: CombinedLoadStates ->
        with(loadState.source.refresh) {
            if (this is LoadState.Loading) showRecentSearch(false)
            binding?.let {
                val empty = searchViewModel.queryChannel.value.isNotEmpty()
                        && this is LoadState.NotLoading
                        && searchAdapter.itemCount == 0
                val error = this is LoadState.Error || empty

                it.list.progressBar.isVisible = this is LoadState.Loading
                it.list.retryButton.isVisible = this is LoadState.Error
                it.list.rvGames.isVisible = this is LoadState.NotLoading && searchAdapter.itemCount != 0
                it.list.error.isVisible = error
                it.list.errorTv.isVisible = error

                if (error) {
                    var res = R.string.network_error
                    var animation = R.raw.nointernet
                    if (this !is LoadState.Error) {
                        res = R.string.no_games_found
                        animation = R.raw.searchnotfound
                    }

                    it.list.error.setAnimation(animation)
                    it.list.error.playAnimation()
                    it.list.errorTv.text = context?.getString(res)
                }
            }

            return@with
        }
    }
}