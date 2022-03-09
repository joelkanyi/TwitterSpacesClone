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

    private val peerComparator = Comparator<HMSPeer> { o1, o2 ->
        Timber.d("Sorting")
        if (o1.isLocal && o2.isLocal) {
            throw Exception("Two locals can't be present at the same time")
        }
        when {
            o1.isLocal -> -1
            o2.isLocal -> 1
            else -> o1.peerID.compareTo(o2.peerID)
        }
    }

    private val _peers: MutableState<List<HMSPeer>> =
        mutableStateOf(emptyList(), neverEqualPolicy())
    val peers: State<List<HMSPeer>> = _peers

    private val _localPeer: MutableState<HMSLocalPeer?> = mutableStateOf(null)
    val localPeer: State<HMSLocalPeer?> = _localPeer

    var loading = false

    fun leaveTheSpace() {
        repository.leaveRoom()
    }

    fun isLocalAudioEnabled(): Boolean? {
        return repository.isLocalAudioEnabled()
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
                        Timber.d("onChangeTrackStateRequest")
                    }

                    override fun onError(error: HMSException) {
                        loading = false
                        Timber.d(error.message)
                    }

                    override fun onJoin(room: HMSRoom) {
                        loading = false
                        _peers.value = room.peerList.asList()
                        _localPeer.value = room.localPeer
                    }

                    override fun onMessageReceived(message: HMSMessage) {
                        Timber.d(message.message)
                    }

                    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
                        Timber.d("There was a peer update: $type")

                        // Handle peer updates.
                        when (type) {
                            HMSPeerUpdate.PEER_JOINED -> _peers.value =
                                _peers.value.plus(peer).sortedWith(peerComparator)
                            HMSPeerUpdate.PEER_LEFT -> _peers.value =
                                _peers.value.filter { currentPeer -> currentPeer.peerID != peer.peerID }
                        }
                    }

                    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
                        Timber.d("Role change request")
                    }

                    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
                        Timber.d("Room update")
                    }

                    override fun onTrackUpdate(
                        type: HMSTrackUpdate,
                        track: HMSTrack,
                        peer: HMSPeer
                    ) {
                        Timber.d("Somebody's audio/video changed")
                        when (type) {
                            HMSTrackUpdate.TRACK_REMOVED -> {
                                Timber.d("Checking, $type, $track")
                                if (track.type == HMSTrackType.AUDIO) {
                                    _peers.value =
                                        _peers.value.filter { currentPeer -> currentPeer.peerID != peer.peerID }
                                            .plus(peer).sortedWith(peerComparator)

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