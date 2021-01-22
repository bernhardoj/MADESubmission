package com.example.madesubmission.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.ui.GameAdapter
import com.example.madesubmission.databinding.FragmentListBinding
import com.example.madesubmission.ui.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()
    private var binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val gameAdapter = GameAdapter {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.GAME_EXTRA, it)
//            startActivity(intent)
//        }
//
//        binding.rvGames.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = gameAdapter
//        }
//
//        searchViewModel.gameLiveData.observe(viewLifecycleOwner, { game ->
//            when (game) {
//                is Resource.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.retryButton.visibility = View.GONE
//                    binding.errorTv.visibility = View.GONE
//                    gameAdapter.clearList()
//                }
//                is Resource.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    if (game.data.isEmpty()) {
//                        binding.errorTv.apply {
//                            text = context.getString(R.string.no_games_found)
//                            visibility = View.VISIBLE
//                        }
//                    } else {
//                        gameAdapter.setGameList(game.data)
//                    }
//                }
//                is Resource.Error -> {
//                    binding.errorTv.apply {
//                        text = game.message
//                        visibility = View.VISIBLE
//                    }
//                    binding.progressBar.visibility = View.GONE
//                    binding.retryButton.visibility = View.VISIBLE
//                }
//            }
//        })
//
//        binding.retryButton.setOnClickListener {
//            searchViewModel.loadAllGames()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}