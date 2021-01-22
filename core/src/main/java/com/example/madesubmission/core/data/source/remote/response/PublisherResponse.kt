package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PublisherResponse(
    @field:SerializedName("name")
    val name: String
)
