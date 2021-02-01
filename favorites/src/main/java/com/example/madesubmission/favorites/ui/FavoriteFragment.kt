package com.example.madesubmission.favorites.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.core.ui.PagedGameAdapter
import com.example.madesubmission.favorites.databinding.FragmentFavoriteBinding
import com.example.madesubmission.favorites.di.viewModelModule
import com.example.madesubmission.ui.detail.DetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        loadKoinModules(viewModelModule)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { binding ->
            val favoriteAdapter = PagedGameAdapter {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.GAME_EXTRA, it)
                startActivity(intent)
            }

            favoriteAdapter.addLoadStateListener { loadState ->
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.rvFavorites.isVisible = loadState.source.refresh is LoadState.NotLoading && favoriteAdapter.itemCount != 0
                binding.emptyTv.isVisible = loadState.source.refresh is LoadState.NotLoading && favoriteAdapter.itemCount == 0
                binding.empty.isVisible = loadState.source.refresh is LoadState.NotLoading && favoriteAdapter.itemCount == 0
            }

            binding.rvFavorites.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = favoriteAdapter
            }

            viewLifecycleOwner.lifecycleScope.launch {
                favoriteViewModel.getFavorites().collectLatest {
                    favoriteAdapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}