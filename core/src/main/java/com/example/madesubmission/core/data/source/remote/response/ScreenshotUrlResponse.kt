package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ScreenshotUrlResponse(
    @field:SerializedName("image")
    val urls: String
)