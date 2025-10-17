package com.discdogs.app.presentation.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.discdogs.app.app.Route
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListDetailViewModel(
    private val libraryRepository: LibraryRepository,
    private val savedStateHandle: SavedStateHandle

) : BaseViewModel<ListDetailState, ListDetailEffect, ListDetailEvent, ListDetailNavigator>() {

    private val _state = MutableStateFlow(ListDetailState())
    override val state: StateFlow<ListDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ListDetailEffect>()
    override val effect: SharedFlow<ListDetailEffect> = _effect


    init {
        loadList(savedStateHandle.toRoute<Route.FavoriteList>().id)

    }


    override fun process(event: ListDetailEvent) {
        when (event) {


            is ListDetailEvent.OnReleaseClick -> {
                navigator?.navigateToReleaseDetail(event.release.id, event.release.thumb)
            }

            is ListDetailEvent.OnRemoveRelease -> {
                removeRelease(event.releaseId)
            }

            ListDetailEvent.OnBackClicked -> navigator?.navigateBack()
        }
    }

    private fun loadList(listId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, listId = listId) }

            try {
                // Get the list name first
                val list = libraryRepository.getListById(listId)
                val listName = list?.name ?: "List"

                // Then load the releases
                libraryRepository.getReleasesInList(listId)
                    .distinctUntilChanged()
                    .collect { releases ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                releases = releases,
                                listName = listName
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    private fun removeRelease(releaseId: Int) {
        val listId = _state.value.listId
        if (listId != null) {
            viewModelScope.launch {
                try {
                    libraryRepository.removeReleaseFromList(listId, releaseId)

                } catch (e: Exception) {

                }
            }
        }
    }
}
