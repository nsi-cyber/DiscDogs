package com.discdogs.app.presentation.detail

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
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val audioRepository: AudioRepository,
    private val networkRepository: NetworkRepository,
    private val externalRepository: ExternalRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailState, DetailEffect, DetailEvent, DetailNavigator>() {

    private var uriHandler: UriHandler? = null

    fun setUriHandler(handler: UriHandler) {
        this.uriHandler = handler
    }
    private val _state =
        MutableStateFlow(DetailState(backgroundImage = savedStateHandle.toRoute<Route.ReleaseDetail>().image))
    override val state: StateFlow<DetailState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DetailEffect>()
    override val effect: SharedFlow<DetailEffect> get() = _effect


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

        getReleaseDetails(
            savedStateHandle.toRoute<Route.ReleaseDetail>().releaseId,
            savedStateHandle.toRoute<Route.ReleaseDetail>().source.toDetailSource()
        )
    }

    override fun process(event: DetailEvent) {
        when (event) {
            DetailEvent.OnBackClicked -> navigator?.navigateBack()
            is DetailEvent.OnPreviewTrack -> getTrackPreview(event.data)
            DetailEvent.OnReleaseTrack -> {
                _state.update {
                    it.copy(
                        isPreviewLoading = false, playingItem = null,
                    )
                }
                audioRepository.stop()
            }

            is DetailEvent.OnLoadBackground -> _state.update {
                it.copy(
                    backgroundImage = event.image
                )
            }

            DetailEvent.OnDismissMoreBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            moreSheetVisible = false,
                        )
                    }
                    _effect.emit(DetailEffect.DismissMoreBottomSheet)

                }
            }

            DetailEvent.OnShowMoreBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            moreSheetVisible = true,
                        )
                    }
                    _effect.emit(DetailEffect.ShowMoreBottomSheet)

                }
            }

            DetailEvent.OnDismissBarcodeBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            barcodeSheetVisible = false,
                        )
                    }
                    _effect.emit(DetailEffect.DismissBarcodeBottomSheet)

                }
            }

            DetailEvent.OnShowBarcodeBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            barcodeSheetVisible = true,
                        )
                    }
                    _effect.emit(DetailEffect.ShowBarcodeBottomSheet)

                }
            }

            is DetailEvent.OnExternalWebsite -> {
                openExternalPlayerLink(event.type)
            }


            DetailEvent.OnOtherReleases -> {
                state.value.releaseDetail?.masterId?.let { masterId ->
                    navigator?.navigateToMastersVersions(
                        masterId
                    )
                }

            }

            DetailEvent.OnToggleFavorite -> {
                toggleFavorite()
            }


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
                            image = state.value.releaseDetail?.thumb.orEmpty()
                        )
                )
            }
            val query =
                "${state.value.releaseDetail?.artists?.firstOrNull()?.name} ${data.title} ${state.value.releaseDetail?.title}"
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
                                        image = state.value.releaseDetail?.thumb.orEmpty()
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
                    errorSnack(message = UiText.DynamicString("R.string.we_couldnt_find_a_preview_for_this_track"))
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isPreviewLoading = false, playingItem = null,
                        )
                    }
                    errorSnack(message = UiText.DynamicString("R.string.we_couldnt_find_a_preview_for_this_track"))


                }
            }
        }
    }

    private fun getReleaseDetails(releaseId: Int, source: DetailSource) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = networkRepository.getReleaseDetail(releaseId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            releaseDetail = result.value.toUiModel(),
                            backgroundImage = result.value?.thumb,
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    errorSnack(UiText.DynamicString("R.string.release_detail_error_message"))
                    _state.update { it.copy(isLoading = false) }
                    navigator?.navigateBack()
                }
            }
        }
    }

    private fun toggleFavorite() {
        _state.value.releaseDetail ?: return
        _state.value.isFavorite


    }

    private fun openExternalPlayerLink(type: ExternalWebsites) {
        val release = _state.value.releaseDetail ?: return
        val artistName = release.artists?.firstOrNull()?.name ?: return
        val title = release.title.orEmpty()



        viewModelScope.launch {
           uriHandler?.openUri(externalRepository.openExternalPlayerLink(buildQuery(artistName, title), type))
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
