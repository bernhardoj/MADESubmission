package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PlatformResponse(
    @field:SerializedName("platform")
    val platformName: PlatformNameResponse
)