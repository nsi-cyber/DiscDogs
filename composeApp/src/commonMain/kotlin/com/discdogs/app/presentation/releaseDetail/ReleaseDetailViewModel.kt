package com.discdogs.app.presentation.releaseDetail

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
import com.discdogs.app.data.repository.LibraryRepository
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

class ReleaseDetailViewModel(
    private val audioRepository: AudioRepository,
    private val networkRepository: NetworkRepository,
    private val externalRepository: ExternalRepository,
    private val libraryRepository: LibraryRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ReleaseDetailState, ReleaseDetailEffect, ReleaseDetailEvent, ReleaseDetailNavigator>() {

    private var uriHandler: UriHandler? = null

    fun setUriHandler(handler: UriHandler) {
        this.uriHandler = handler
    }

    private val _state =
        MutableStateFlow(ReleaseDetailState(backgroundImage = savedStateHandle.toRoute<Route.ReleaseDetail>().image))
    override val state: StateFlow<ReleaseDetailState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReleaseDetailEffect>()
    override val effect: SharedFlow<ReleaseDetailEffect> get() = _effect


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
            savedStateHandle.toRoute<Route.ReleaseDetail>().source
        )
    }

    override fun process(event: ReleaseDetailEvent) {
        when (event) {
            ReleaseDetailEvent.OnBackClicked -> {
                audioRepository.cleanup()
                navigator?.navigateBack()
            }

            is ReleaseDetailEvent.OnPreviewTrack -> getTrackPreview(event.data)
            ReleaseDetailEvent.OnReleaseTrack -> {
                _state.update {
                    it.copy(
                        isPreviewLoading = false, playingItem = null,
                    )
                }
                audioRepository.stop()
            }

            is ReleaseDetailEvent.OnLoadBackground -> _state.update {
                it.copy(
                    backgroundImage = event.image
                )
            }

            ReleaseDetailEvent.OnDismissMoreBottomSheet -> {
                viewModelScope.launch {
                    _effect.emit(ReleaseDetailEffect.DismissMoreBottomSheet)
                    delay(100)
                    _state.update {
                        it.copy(
                            moreSheetVisible = false,
                        )
                    }

                }
            }

            ReleaseDetailEvent.OnShowMoreBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            moreSheetVisible = true,
                        )
                    }
                    _effect.emit(ReleaseDetailEffect.ShowMoreBottomSheet)

                }
            }

            ReleaseDetailEvent.OnDismissBarcodeBottomSheet -> {
                viewModelScope.launch {
                    _effect.emit(ReleaseDetailEffect.DismissBarcodeBottomSheet)
                    delay(100)

                    _state.update {
                        it.copy(
                            barcodeSheetVisible = false,
                        )
                    }

                }
            }

            ReleaseDetailEvent.OnShowBarcodeBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            barcodeSheetVisible = true,
                        )
                    }
                    _effect.emit(ReleaseDetailEffect.ShowBarcodeBottomSheet)

                }
            }

            is ReleaseDetailEvent.OnExternalWebsite -> {
                audioRepository.cleanup()
                openExternalPlayerLink(event.type)
            }

            ReleaseDetailEvent.OnOtherReleases -> {
                audioRepository.cleanup()
                state.value.releaseDetail?.masterId?.let { masterId ->
                    navigator?.navigateToMastersVersions(
                        masterId, savedStateHandle.toRoute<Route.ReleaseDetail>().source
                    )
                }

            }

            ReleaseDetailEvent.OnToggleFavorite -> {
                toggleFavorite()
            }

            ReleaseDetailEvent.OnShare -> uriHandler?.openUri(_state.value.releaseDetail?.uri.toString())


            is ReleaseDetailEvent.OnLoadReleaseDetails -> getReleaseDetails(
                event.releaseId,
                event.source
            )


            ReleaseDetailEvent.OnShowSaveToListBottomSheet -> {
                viewModelScope.launch {
                    loadLists()
                    _state.update {
                        it.copy(
                            saveToListSheetVisible = true,
                        )
                    }
                    _effect.emit(ReleaseDetailEffect.ShowSaveToListBottomSheet)
                }
            }

            ReleaseDetailEvent.OnDismissSaveToListBottomSheet -> {
                viewModelScope.launch {
                    _effect.emit(ReleaseDetailEffect.DismissSaveToListBottomSheet)
                    delay(100)

                    _state.update {
                        it.copy(
                            showCreateListBottomSheet = false,
                            saveToListSheetVisible = false
                        )
                    }
                }
            }

            is ReleaseDetailEvent.OnAddToList -> {
                toggleList(event.listId)
            }

            ReleaseDetailEvent.OnCreateNewList -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showCreateListBottomSheet = true,
                        )
                    }
                }
            }

            is ReleaseDetailEvent.OnCreateList -> {
                createList(event.name)
            }

            ReleaseDetailEvent.OnDismissCreateListBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showCreateListBottomSheet = false,
                        )
                    }
                }
            }
        }
    }


    private fun loadLists() {
        val releaseId = _state.value.releaseDetail?.id ?: return
        viewModelScope.launch {
            libraryRepository.getAllLists()
                .collect { lists ->
                    _state.update { it.copy(lists = lists) }
                }
        }

        // Also load which lists contain this release
        viewModelScope.launch {
            libraryRepository.getListsContainingRelease(releaseId)
                .collect { listsContainingRelease ->
                    _state.update { it.copy(releaseInLists = listsContainingRelease) }
                }
        }
    }

    private fun toggleList(listId: Long) {
        val releaseDetail = _state.value.releaseDetail ?: return
        val releaseId = releaseDetail.id
        val isInList = _state.value.releaseInLists.contains(listId)

        viewModelScope.launch {
            try {
                if (isInList) {
                    // Remove from list
                    libraryRepository.removeReleaseFromList(listId, releaseId)
                } else {
                    // Add to list
                    libraryRepository.addReleaseToList(listId, releaseDetail)
                }
                // Refresh the lists containing this release
                loadLists()
            } catch (e: Exception) {
                errorSnack(message = UiText.DynamicString(e.toString()))
            }
            _effect.emit(ReleaseDetailEffect.DismissSaveToListBottomSheet)
            delay(100)

            _state.update {
                it.copy(
                    showCreateListBottomSheet = false,
                    saveToListSheetVisible = false
                )
            }

        }
    }

    private fun createList(name: String) {
        viewModelScope.launch {
            try {
                val listId = libraryRepository.createList(name)

                // Automatically add the current release to the newly created list
                val currentRelease = _state.value.releaseDetail
                if (currentRelease != null) {
                    try {
                        libraryRepository.addReleaseToList(listId, currentRelease)
                    } catch (e: Exception) {
                        // If adding to list fails, still show success for list creation
                        // but log the error
                        errorSnack(message = UiText.DynamicString(e.toString()))
                    }
                }

                _effect.emit(ReleaseDetailEffect.DismissSaveToListBottomSheet)
                delay(100)

                _state.update {
                    it.copy(
                        showCreateListBottomSheet = false,
                        saveToListSheetVisible = false
                    )
                }
                loadLists() // Refresh lists
            } catch (e: Exception) {
                errorSnack(message = UiText.DynamicString(e.toString()))
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

    private fun getReleaseDetails(releaseId: Int, source: String) {
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
                    // Check if release is favorite and observe changes
                    result.value?.toUiModel()?.let { vinylDetail ->
                        viewModelScope.launch {
                            libraryRepository.isFavorite(vinylDetail.id).collect { isFavorite ->
                                _state.update { currentState ->
                                    currentState.copy(isFavorite = isFavorite)
                                }
                            }
                        }

                        // Add to recent items based on source
                        if (source == "SEARCH" ||
                            source == "SCAN"
                        ) {
                            viewModelScope.launch {
                                libraryRepository.addRecentRelease(vinylDetail, source)
                            }
                        }
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

    private fun toggleFavorite() {
        val releaseDetail = _state.value.releaseDetail ?: return
        val isFavorite = _state.value.isFavorite

        viewModelScope.launch {
            if (isFavorite) {
                libraryRepository.removeFromFavorites(releaseDetail.id)
            } else {
                libraryRepository.addToFavorites(releaseDetail)
            }
            _effect.emit(ReleaseDetailEffect.DismissSaveToListBottomSheet)
            delay(100)

            _state.update {
                it.copy(
                    showCreateListBottomSheet = false,
                    saveToListSheetVisible = false
                )
            }
        }
    }

    private fun openExternalPlayerLink(type: ExternalWebsites) {
        val release = _state.value.releaseDetail ?: return
        val artistName = release.artists?.firstOrNull()?.name ?: return
        val title = release.title.orEmpty()



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
