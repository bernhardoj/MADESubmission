package com.example.madesubmission.ui.explore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
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
    private lateinit var gameAdapter: GameAdapter

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
            initViewHolder(binding)

            exploreListViewModel.gameLiveData.observe(viewLifecycleOwner) { state ->
                binding.progressBar.isVisible = state is Resource.Loading
                binding.retryButton.isVisible = state is Resource.Error
                binding.errorTv.isVisible = state is Resource.Error
                binding.error.isVisible = state is Resource.Error

                if (state is Resource.Success) gameAdapter.setGameList(state.data)
                if (state is Resource.Error) {
                    binding.errorTv.text = state.message
                    binding.error.setAnimation(R.raw.nointernet)
                    binding.error.playAnimation()
                }
            }

            binding.retryButton.setOnClickListener {
                exploreListViewModel.loadAllGames()
            }
        }
    }

    private fun initViewHolder(binding: FragmentListBinding) {
        gameAdapter = GameAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.GAME_EXTRA, it)
            startActivity(intent)
        }

        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gameAdapter
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