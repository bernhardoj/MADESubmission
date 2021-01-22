package com.example.madesubmission.ui.explore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.ui.GameAdapter
import com.example.madesubmission.databinding.FragmentListBinding
import com.example.madesubmission.ui.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val ARG_PLATFORM = "platform"

class ExploreListFragment : Fragment() {
    private val exploreListViewModel: ExploreListViewModel by viewModel {
        parametersOf(platform)
    }

    private var binding: FragmentListBinding? = null
    private var platform: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            platform = it.getString(ARG_PLATFORM)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
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

            binding.rvGames.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = gameAdapter
            }

            exploreListViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
                when (game) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.retryButton.visibility = View.GONE
                        binding.errorTv.visibility = View.GONE
                        gameAdapter.clearList()
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        gameAdapter.setGameList(game.data)
                    }
                    is Resource.Error -> {
                        binding.errorTv.apply {
                            text = game.message
                            visibility = View.VISIBLE
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.retryButton.visibility = View.VISIBLE
                    }
                }
            }

            binding.retryButton.setOnClickListener {
                exploreListViewModel.loadAllGames()
            }
        }
    }

    companion object {
        fun newInstance(platform: String) =
                ExploreListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PLATFORM, platform)
                    }
                }
    }
}