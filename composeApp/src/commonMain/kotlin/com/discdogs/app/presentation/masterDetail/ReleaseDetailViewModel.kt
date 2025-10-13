package com.discdogs.app.presentation.masterDetail

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.discdogs.app.app.Route
import com.discdogs.app.core.audioPlayer.AudioRepository
import com.discdogs.app.core.audioPlayer.PlaybackState
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.core.presentation.UiText
import com.discdogs.app.domain.ExternalRepository
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.PlayingItem
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.toUiModel
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.release_detail_error_message
import discdog.composeapp.generated.resources.we_couldnt_find_a_preview_for_this_track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MasterDetailViewModel(
    private val audioRepository: AudioRepository,
    private val networkRepository: NetworkRepository,
    private val externalRepository: ExternalRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MasterDetailState, MasterDetailEffect, MasterDetailEvent, MasterDetailNavigator>() {

    private var uriHandler: UriHandler? = null

    fun setUriHandler(handler: UriHandler) {
        this.uriHandler = handler
    }

    private val _state =
        MutableStateFlow(MasterDetailState(backgroundImage = savedStateHandle.toRoute<Route.MasterDetail>().image))
    override val state: StateFlow<MasterDetailState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MasterDetailEffect>()
    override val effect: SharedFlow<MasterDetailEffect> get() = _effect


    init {
        viewModelScope.launch {
            audioRepository.playerState.collect {
                if (it == PlaybackState.ENDED) {
                    _state.update {
                        it.copy(
                            isPreviewLoading = false, playingItem = null,
                        )
                    }
                }
            }
        }

        getMasterDetails(
            savedStateHandle.toRoute<Route.MasterDetail>().masterId,
            savedStateHandle.toRoute<Route.MasterDetail>().source
        )
    }

    override fun process(event: MasterDetailEvent) {
        when (event) {
            MasterDetailEvent.OnBackClicked -> {
                audioRepository.cleanup()
                navigator?.navigateBack()
            }

            is MasterDetailEvent.OnPreviewTrack -> getTrackPreview(event.data)
            MasterDetailEvent.OnReleaseTrack -> {
                _state.update {
                    it.copy(
                        isPreviewLoading = false, playingItem = null,
                    )
                }
                audioRepository.stop()
            }

            is MasterDetailEvent.OnLoadBackground -> _state.update {
                it.copy(
                    backgroundImage = event.image
                )
            }

            MasterDetailEvent.OnDismissMoreBottomSheet -> {
                viewModelScope.launch {
                    _effect.emit(MasterDetailEffect.DismissMoreBottomSheet)
                    delay(100)
                    _state.update {
                        it.copy(
                            moreSheetVisible = false,
                        )
                    }

                }
            }

            MasterDetailEvent.OnShowMoreBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            moreSheetVisible = true,
                        )
                    }
                    _effect.emit(MasterDetailEffect.ShowMoreBottomSheet)

                }
            }


            is MasterDetailEvent.OnExternalWebsite -> {
                audioRepository.cleanup()
                openExternalPlayerLink(event.type)
            }

            MasterDetailEvent.OnOtherReleases -> {
                audioRepository.cleanup()
                state.value.masterDetail?.masterId?.let { masterId ->
                    navigator?.navigateToMastersVersions(
                        masterId, savedStateHandle.toRoute<Route.MasterDetail>().source
                    )
                }

            }


            MasterDetailEvent.OnShare -> uriHandler?.openUri(_state.value.masterDetail?.uri.toString())


            is MasterDetailEvent.OnLoadMasterDetails -> getMasterDetails(
                event.masterId,
                event.source
            )

        }
    }


    private fun getTrackPreview(data: TrackListUiModel) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isPreviewLoading = true, playingItem =
                        PlayingItem(
                            id = data.id,
                            title = data.title,
                            image = state.value.masterDetail?.thumb.orEmpty()
                        )
                )
            }
            val query =
                "${state.value.masterDetail?.artists?.firstOrNull()?.name} ${data.title} ${state.value.masterDetail?.title}"
            when (val result = networkRepository.searchSongPreview(query)) {
                is Resource.Success -> {
                    if (result.value != null) {
                        _state.update {
                            it.copy(
                                playingItem =
                                    PlayingItem(
                                        id = data.id,
                                        title = data.title,
                                        preview = result.value,
                                        image = state.value.masterDetail?.thumb.orEmpty()
                                    ),
                                isPreviewLoading = false
                            )
                        }
                        audioRepository.play(result.value)
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            playingItem = null,
                            isPreviewLoading = false
                        )
                    }
                    errorSnack(message = UiText.StringResourceId(Res.string.we_couldnt_find_a_preview_for_this_track))
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isPreviewLoading = false, playingItem = null,
                        )
                    }
                    errorSnack(message = UiText.StringResourceId(Res.string.we_couldnt_find_a_preview_for_this_track))


                }
            }
        }
    }

    private fun getMasterDetails(masterId: Int, source: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = networkRepository.getMasterDetail(masterId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            masterDetail = result.value.toUiModel(),
                            backgroundImage = result.value?.images?.firstOrNull()?.uri,
                            isLoading = false
                        )
                    }

                }

                is Resource.Error -> {
                    errorSnack(message = UiText.StringResourceId(Res.string.release_detail_error_message))
                    _state.update { it.copy(isLoading = false) }
                    navigator?.navigateBack()
                }
            }
        }
    }


    private fun openExternalPlayerLink(type: ExternalWebsites) {
        val master = _state.value.masterDetail ?: return
        val artistName = master.artists?.firstOrNull()?.name ?: return
        val title = master.title.orEmpty()



        viewModelScope.launch {
            uriHandler?.openUri(
                externalRepository.openExternalPlayerLink(
                    buildQuery(
                        artistName,
                        title
                    ), type
                )
            )
        }
    }

    private fun buildQuery(artist: String, title: String): String {
        return listOf(artist, title)
            .joinToString("+")
            .replace("\\s+".toRegex(), "+")
    }


    override fun onCleared() {
        super.onCleared()
    }
}
