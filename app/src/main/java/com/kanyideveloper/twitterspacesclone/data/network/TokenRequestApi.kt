package com.kanyideveloper.twitterspacesclone.data.network

import com.kanyideveloper.twitterspacesclone.data.network.request.TokenRequest
import com.kanyideveloper.twitterspacesclone.data.network.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRequestApi {
    @POST("api/token")
    suspend fun getToken(@Body tokenRequest: TokenRequest): TokenResponse
}