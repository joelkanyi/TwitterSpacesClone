package com.kanyideveloper.twitterspacesclone.screens.space


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.twitterspacesclone.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.*
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest
import timber.log.Timber
import javax.inject.Inject
import kotlin.Comparator

@HiltViewModel
class SpaceViewModel @Inject constructor(
    private val repository: SpaceRepository
) : ViewModel() {

    private val _peers: MutableState<List<HMSPeer>> =
        mutableStateOf(emptyList(), neverEqualPolicy())
    val peers: State<List<HMSPeer>> = _peers

    private val _localPeer: MutableState<HMSLocalPeer?> = mutableStateOf(null)
    val localPeer: State<HMSLocalPeer?> = _localPeer

    var loading = false

    fun leaveTheSpace() {
        repository.leaveRoom()
    }

    fun isLocalAudioEnabled(): Boolean {
        return repository.isLocalAudioEnabled() == true
    }

    fun setLocalAudioEnabled(enabled: Boolean) {
        repository.setLocalAudioEnabled(enabled)
    }

    fun getNameInitials(name: String): String {
        val words = name.uppercase().trim()
        val wordss = words.split("[,.!?\\s]+".toRegex())

        return if (wordss.size >= 2) {
            "${wordss[0].first()}${wordss[1].first()}"
        } else if (wordss.size <= 1) {
            words.substring(0, 2)
        } else {
            "${wordss[0].first()}"
        }
    }

    fun startMeeting(name: String) {
        loading = true
        viewModelScope.launch {
            val token = repository.requestToken(name).token

            repository.joinRoom(
                name,
                token,
                object : HMSUpdateListener {
                    override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) {
                        Timber.d("onChangeTrackStateRequest, track: ${details.track}, requestedBy: ${details.requestedBy}, mute: ${details.mute}")
                    }

                    override fun onError(error: HMSException) {
                        loading = false
                        Timber.d("An error occurred: ${error.message}")
                    }

                    override fun onJoin(room: HMSRoom) {
                        Timber.d("onJoin: ${room.name}")
                        loading = false
                        _peers.value = room.peerList.asList()
                        _localPeer.value = room.localPeer
                    }

                    override fun onMessageReceived(message: HMSMessage) {
                        Timber.d("Message: ${message.message}")
                    }

                    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
                        Timber.d("There was a peer update: $type peer: $peer")

                        // Handle peer updates.
                        when (type) {
                            HMSPeerUpdate.PEER_JOINED -> _peers.value =
                                _peers.value.plus(peer)
                            HMSPeerUpdate.PEER_LEFT -> _peers.value =
                                _peers.value.filter { currentPeer -> currentPeer.peerID != peer.peerID }
                        }
                    }

                    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
                        Timber.d("Role change request: suggested role: ${request.suggestedRole}, by: ${request.requestedBy} ")
                    }

                    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
                        Timber.d("Room update: type: ${type.name} room: ${hmsRoom.name}")
                    }

                    override fun onTrackUpdate(
                        type: HMSTrackUpdate,
                        track: HMSTrack,
                        peer: HMSPeer
                    ) {
                        Timber.d("Somebody's audio/video changed: type: $type, track: $track, peer: $peer")
                        when (type) {
                            HMSTrackUpdate.TRACK_REMOVED -> {
                                Timber.d("Checking, $type, $track")
                                if (track.type == HMSTrackType.AUDIO) {
                                    _peers.value =
                                        _peers.value.filter { currentPeer -> currentPeer.peerID != peer.peerID }
                                            .plus(peer)

                                } else {
                                    Timber.d("Not processed, $type, $track")
                                }
                            }
                            HMSTrackUpdate.TRACK_DESCRIPTION_CHANGED -> Timber.d("Other mute/unmute $type, $track")
                        }
                    }
                })
        }
    }
}