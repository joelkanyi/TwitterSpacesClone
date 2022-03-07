package com.kanyideveloper.twitterspacesclone.screens.space


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.twitterspacesclone.data.repository.SpaceRepository
import com.kanyideveloper.twitterspacesclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import live.hms.video.error.HMSException
import live.hms.video.media.settings.HMSAudioTrackSettings
import live.hms.video.media.settings.HMSTrackSettings
import live.hms.video.media.tracks.*
import live.hms.video.sdk.HMSActionResultListener
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate
import live.hms.video.sdk.models.role.HMSRole
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest
import live.hms.video.utils.HMSCoroutineScope
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.collections.ArrayList

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

    fun startMeeting(name: String) {
        loading = true
        viewModelScope.launch {
            val token = repository.login(name).token

            repository.joinRoom(
                name,
                token,
                object : HMSUpdateListener {
                    override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) {
                        Timber.d("onChangeTrackStateRequest")
                    }

                    override fun onError(error: HMSException) {
                        loading = false
                        Timber.d("An Error occurred")
                        Timber.d(error.message)
                    }

                    override fun onJoin(room: HMSRoom) {
                        loading = false
                        Timber.d("Room joined")
                        _peers.value = room.peerList.asList()
                        Timber.d("Peers: ${peers.value}")
                    }

                    override fun onMessageReceived(message: HMSMessage) {
                        Timber.d(message.message)
                    }

                    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
                        // Handle peer updates.
                        when (type) {
                            HMSPeerUpdate.PEER_JOINED -> _peers.value =
                                _peers.value.plus(peer).sortedWith(peerComparator)
                            HMSPeerUpdate.PEER_LEFT -> _peers.value =
                                _peers.value.filter { currentPeer -> currentPeer.peerID != peer.peerID }
                        }
                        Timber.d("There was a peer update: $type")
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
                            HMSTrackUpdate.TRACK_MUTED,
                            HMSTrackUpdate.TRACK_UNMUTED,
                            HMSTrackUpdate.TRACK_ADDED,
                            HMSTrackUpdate.TRACK_REMOVED
                            -> {
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