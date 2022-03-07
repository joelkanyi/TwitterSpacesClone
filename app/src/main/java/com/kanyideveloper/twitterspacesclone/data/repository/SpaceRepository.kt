package com.kanyideveloper.twitterspacesclone.data.repository

import com.kanyideveloper.twitterspacesclone.data.network.TokenRequestApi
import com.kanyideveloper.twitterspacesclone.data.network.request.TokenRequest
import com.kanyideveloper.twitterspacesclone.data.network.response.TokenResponse
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import javax.inject.Inject

class SpaceRepository @Inject constructor(
    private val api: TokenRequestApi,
    private val hmsSdk: HMSSDK
) {
    /**
     * Inject the HMSSDK - Done
     * Token Request API - Done
     * */

    /**
     * Define a function to join a selected space (under the hood -> request for the token)
     * Define a function to join a room
     * Define a function to leave a room
     * Define a function to mute and unmute
     * */

    suspend fun login(
        userName: String,
        roomId: String = "620b0d326f2b876d58ef3bc7"
    ): TokenResponse {
        return api.getToken(TokenRequest(userId = userName, roomId = roomId, role = "speaker"))
    }

    fun joinRoom(userName: String, authToken: String, updateListener: HMSUpdateListener) {
        val config = HMSConfig(
            userName = userName,
            authtoken = authToken
        )

        hmsSdk.join(config, updateListener)
    }

    fun leaveRoom() {
        hmsSdk.leave()
    }

    fun setLocalAudioEnabled(enabled: Boolean) {
        hmsSdk.getLocalPeer()?.audioTrack?.apply {
            setMute(!enabled)
        }
    }

    fun isLocalAudioEnabled(): Boolean? {
        return hmsSdk.getLocalPeer()?.audioTrack?.isMute?.not()
    }
}