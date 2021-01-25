package com.example.madesubmission.ui.search

import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.ui.GameAdapter
import com.example.madesubmission.databinding.FragmentSearchBinding
import com.example.madesubmission.ui.detail.DetailActivity
import com.example.madesubmission.ui.search.provider.MySuggestionProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()
    private var binding: FragmentSearchBinding? = null
    private var job: Job? = null

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
            val gameAdapter = GameAdapter {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.GAME_EXTRA, it)
                startActivity(intent)
            }

            binding.list.rvGames.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = gameAdapter
            }

            binding.searchGame.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    job?.cancel()
                    job = lifecycleScope.launch {
                        searchViewModel.queryChannel.send(newText.toString())
                    }
                    return true
                }
            })

            searchViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
                with(binding.list) {
                    when (game) {
                        is Resource.Loading -> {
                            binding.recentSearchHeading.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                            retryButton.visibility = View.GONE
                            errorTv.visibility = View.GONE
                            gameAdapter.clearList()
                        }
                        is Resource.Success -> {
                            // Save recent query
                            SearchRecentSuggestions(
                                context,
                                MySuggestionProvider.AUTHORITY,
                                MySuggestionProvider.MODE
                            ).saveRecentQuery(searchViewModel.queryChannel.value, null)

                            progressBar.visibility = View.GONE
                            if (game.data.isEmpty()) {
                                errorTv.apply {
                                    text = context.getString(R.string.no_games_found)
                                    visibility = View.VISIBLE
                                }
                            } else {
                                gameAdapter.setGameList(game.data)
                            }
                        }
                        is Resource.Error -> {
                            errorTv.apply {
                                text = game.message
                                visibility = View.VISIBLE
                            }
                            progressBar.visibility = View.GONE
                            retryButton.visibility = View.VISIBLE
                        }
                    }
                }
            }

            binding.list.retryButton.setOnClickListener {
                searchViewModel.searchGames()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}