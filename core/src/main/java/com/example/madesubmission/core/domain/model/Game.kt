package com.example.madesubmission.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val platforms: String,
    val updated: Int,
    val isFavorite: Boolean,
    val platformId: String
) : Parcelable
