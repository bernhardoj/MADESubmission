package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DeveloperResponse(
    @field:SerializedName("name")
    val name: String
)
