package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ScreenshotResponse(
    @field:SerializedName("results")
    val screenshotUrl: List<ScreenshotUrlResponse>
)