package com.kanyideveloper.twitterspacesclone.data.network.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token: String
)
