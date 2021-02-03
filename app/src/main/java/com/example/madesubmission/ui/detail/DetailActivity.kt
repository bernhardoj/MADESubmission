package com.example.madesubmission.ui.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madesubmission.R
import com.example.madesubmission.core.GlideApp
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.databinding.ActivityDetailBinding
import com.google.android.material.appbar.AppBarLayout
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {
    companion object {
        const val GAME_EXTRA = "GAME_EXTRA"
    }

    private lateinit var game: Game
    private lateinit var gameDetail: GameDetail
    private lateinit var screenshotAdapter: ScreenshotAdapter
    private var favoriteMenuItem: MenuItem? = null
    private var isFavorite = false
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val detailViewModel: DetailViewModel by viewModel {
        parametersOf(game.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        game = intent.getParcelableExtra(GAME_EXTRA)!!
        binding.toolbarLayout.title = game.name
        binding.toolbarLayout.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        binding.appBar.addOnOffsetChangedListener(offsetListener)

        initViewHolder()

        detailViewModel.gameDetailLiveData.observe(this, { state ->
            binding.appBar.isVisible = state is Resource.Success
            binding.content.contentLayout.isVisible = state is Resource.Success
            binding.progressBar.isVisible = state is Resource.Loading
            binding.errorLayout.isVisible = state is Resource.Error

            if (state is Resource.Error) binding.errorTv.text = state.message
            if (state is Resource.Success) {
                gameDetail = state.data
                updateView()
            }
        })

        detailViewModel.favoriteLiveData.observe(this) { isFavorite ->
            this.isFavorite = isFavorite
            changeIcon()
        }

        binding.retryButton.setOnClickListener {
            detailViewModel.loadDetail()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        favoriteMenuItem = menu?.getItem(0)
        changeIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_favorite -> {
                detailViewModel.updateFavorite(!isFavorite, game)
                true
            }
            else -> false
        }
    }

    private fun initViewHolder() {
        screenshotAdapter = ScreenshotAdapter()
        binding.content.screenshotRv.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = screenshotAdapter
        }
    }

    private fun updateView() {
        // About section
        GlideApp.with(this)
            .load(game.imageUrl)
            .placeholder(ColorDrawable(Color.GRAY))
            .into(binding.gameImage)
        with(binding.content) {
            gameTitle.text = game.name
            gameDesc.text =
                HtmlCompat.fromHtml(gameDetail.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toString()
            gameReleased.text = gameDetail.releaseDate
            gameDevelopers.text = gameDetail.developers
            gameGenres.text = game.genres
            gamePlatforms.text = gameDetail.platforms
            gamePublishers.text = gameDetail.publishers
            rating.text = game.rating.toString()
        }

        // Screenshot section
        if (gameDetail.screenshots.isEmpty())
            binding.content.noScreenshots.visibility = View.VISIBLE
        else {
            binding.content.screenshotRv.visibility = View.VISIBLE
            screenshotAdapter.setScreenshots(gameDetail.screenshots)
        }

        isFavorite = gameDetail.isFavorite
        detailViewModel.setFavorite(isFavorite)
    }

    private fun changeIcon() {
        favoriteMenuItem?.setIcon(
            if (isFavorite) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
    }

    private val offsetListener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        val range = ((appBarLayout.totalScrollRange*0.69344).toInt())..appBarLayout.totalScrollRange
        when (abs(verticalOffset)) {
            in range -> {
                Log.d("TAG", "$verticalOffset")
                binding.toolbar.background = null
            }
            else -> binding.toolbar.background =
                ContextCompat.getDrawable(this, R.drawable.gradient_overlay)
        }
    }
}