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
import com.discdogs.app.data.repository.LibraryRepository
import com.discdogs.app.domain.ExternalRepository
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.toUiModel
import kotlinx.coroutines.delay
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
    private val libraryRepository: LibraryRepository,
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
            savedStateHandle.toRoute<Route.ReleaseDetail>().source
        )
    }

    override fun process(event: DetailEvent) {
        when (event) {
            DetailEvent.OnBackClicked -> {
                audioRepository.cleanup()
                navigator?.navigateBack()
            }
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
                    _effect.emit(DetailEffect.DismissMoreBottomSheet)
                    delay(100)
                    _state.update {
                        it.copy(
                            moreSheetVisible = false,
                        )
                    }

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
                    _effect.emit(DetailEffect.DismissBarcodeBottomSheet)
                    delay(100)

                    _state.update {
                        it.copy(
                            barcodeSheetVisible = false,
                        )
                    }

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
                audioRepository.cleanup()
                openExternalPlayerLink(event.type)
            }
            DetailEvent.OnOtherReleases -> {
                audioRepository.cleanup()
                state.value.releaseDetail?.masterId?.let { masterId ->
                    navigator?.navigateToMastersVersions(
                        masterId
                    )
                }

            }
            DetailEvent.OnToggleFavorite -> {
                toggleFavorite()
            }
            DetailEvent.OnShare -> uriHandler?.openUri(_state.value.releaseDetail?.uri.toString())


            is DetailEvent.OnLoadReleaseDetails -> getReleaseDetails(event.releaseId, event.source)


            DetailEvent.OnShowSaveToListBottomSheet -> {
                viewModelScope.launch {
                    loadLists()
                    _state.update {
                        it.copy(
                            saveToListSheetVisible = true,
                        )
                    }
                    _effect.emit(DetailEffect.ShowSaveToListBottomSheet)
                }
            }

            DetailEvent.OnDismissSaveToListBottomSheet -> {
                viewModelScope.launch {
                    _effect.emit(DetailEffect.DismissSaveToListBottomSheet)
                    delay(100)

                    _state.update {
                        it.copy(
                            showCreateListDialog = false,
                            saveToListSheetVisible = false
                        )
                    }
                }
            }

            is DetailEvent.OnAddToList -> {
                toggleList(event.listId)
            }

            DetailEvent.OnCreateNewList -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showCreateListDialog = true,
                        )
                    }
                }
            }

            is DetailEvent.OnCreateList -> {
                createList(event.name)
            }

            DetailEvent.OnDismissCreateListDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showCreateListDialog = false,
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
                errorSnack(message = e.toString())
            }
            _effect.emit(DetailEffect.DismissSaveToListBottomSheet)
            delay(100)

            _state.update {
                it.copy(
                    showCreateListDialog = false,
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
                        errorSnack(message = e.toString())
                    }
                }

                _effect.emit(DetailEffect.DismissSaveToListBottomSheet)
                delay(100)

                _state.update {
                    it.copy(
                        showCreateListDialog = false,
                        saveToListSheetVisible = false
                    )
                }
                loadLists() // Refresh lists
            } catch (e: Exception) {
                errorSnack(message = e.toString())
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
                    errorSnack(message = ("R.string.we_couldnt_find_a_preview_for_this_track"))
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isPreviewLoading = false, playingItem = null,
                        )
                    }
                    errorSnack(message = ("R.string.we_couldnt_find_a_preview_for_this_track"))


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
                    errorSnack(("R.string.release_detail_error_message"))
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
            _effect.emit(DetailEffect.DismissSaveToListBottomSheet)
            delay(100)

            _state.update {
                it.copy(
                    showCreateListDialog = false,
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
