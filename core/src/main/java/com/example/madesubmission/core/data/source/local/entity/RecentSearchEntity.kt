package com.example.madesubmission.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearchEntity(
    @PrimaryKey
    val query: String
)