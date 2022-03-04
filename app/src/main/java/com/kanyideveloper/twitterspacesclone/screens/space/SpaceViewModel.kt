package com.kanyideveloper.twitterspacesclone.screens.space


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.twitterspacesclone.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.media.tracks.HMSTrackType
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest
import live.hms.video.utils.HMSCoroutineScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SpaceViewModel @Inject constructor(
    private val repository: SpaceRepository, private val hmssdk: HMSSDK
) : ViewModel() {

    private val _peers: MutableState<List<HMSPeer>> =
        mutableStateOf(emptyList(), neverEqualPolicy())
    val peers: State<List<HMSPeer>> = _peers


    init {
        viewModelScope.launch {
           // startMeeting("Joel")
        }
    }

    private suspend fun startMeeting(name: String) {
        viewModelScope.launch {
            val token = repository.login(name).token

            Timber.d("Token: $token")
            repository.joinRoom(name, token, object : HMSUpdateListener {
                override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) {
                    Timber.d("onChangeTrackStateRequest")
                }

                override fun onError(error: HMSException) {
                    Timber.d("An Error occurred")
                    Timber.d(error.message)
                }

                override fun onJoin(room: HMSRoom) {
                    Timber.d("Room joined")
                    _peers.value = room.peerList.asList()
                    Timber.d("Peers: ${_peers.value.get(0).name}")
                }

                override fun onMessageReceived(message: HMSMessage) {
                    Timber.d(message.message)
                }

                override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
                    Timber.d("Peer update")
                }

                override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
                    Timber.d("Role change request")
                }

                override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
                    Timber.d("Room update")
                }

                override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
                    Timber.d("Somebody's audio/video changed")
                }
            }
            )
        }
    }
}