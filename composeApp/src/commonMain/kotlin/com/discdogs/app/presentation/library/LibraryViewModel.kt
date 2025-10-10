package com.discdogs.app.presentation.library

import androidx.lifecycle.viewModelScope
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

class LibraryViewModel(
    private val libraryRepository: LibraryRepository,
) : BaseViewModel<LibraryState, LibraryEffect, LibraryEvent, LibraryNavigator>() {

    private val _state = MutableStateFlow(LibraryState())
    override val state: StateFlow<LibraryState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LibraryEffect>()
    override val effect: SharedFlow<LibraryEffect> get() = _effect


    override fun process(event: LibraryEvent) {
        when (event) {
            LibraryEvent.OnBackClicked -> navigator?.navigateBack()
            is LibraryEvent.OnReleaseDetail -> {
                navigator?.navigateToReleaseDetail(event.data.id, event.data.thumb)
            }

            is LibraryEvent.OnRemoveFromFavorites -> {
                removeFromFavorites(event.releaseId)
            }

            LibraryEvent.OnFavoritesTabSelected -> {
                _state.update { it.copy(selectedTab = LibraryTab.FAVORITES) }
            }

            LibraryEvent.OnListsTabSelected -> {
                _state.update { it.copy(selectedTab = LibraryTab.LISTS) }
                loadLists()
            }

            is LibraryEvent.OnListSelected -> {
                navigator?.navigateToListDetail(event.listId)
            }

            LibraryEvent.OnCreateNewList -> {
                _state.update { it.copy(showCreateListDialog = true) }
            }

            is LibraryEvent.OnCreateList -> {
                createList(event.name)
            }

            LibraryEvent.OnDismissCreateListDialog -> {
                _state.update { it.copy(showCreateListDialog = false) }
            }

            is LibraryEvent.OnDeleteList -> {
                deleteList(event.listId)
            }
        }
    }

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            libraryRepository.getAllFavorites()
                .distinctUntilChanged()
                .collect { favorites ->
                    _state.update { it.copy(favoriteReleases = favorites) }
                }
        }
    }

    private fun loadLists() {
        viewModelScope.launch {
            libraryRepository.getAllLists()
                .distinctUntilChanged()
                .collect { lists ->
                    _state.update { it.copy(lists = lists) }
                }
        }
    }


    private fun createList(name: String) {
        viewModelScope.launch {
            try {
                libraryRepository.createList(name)
                _state.update { it.copy(showCreateListDialog = false) }
            } catch (e: Exception) {
            }
        }
    }

    private fun deleteList(listId: Long) {
        viewModelScope.launch {
            try {
                libraryRepository.deleteList(listId)
            } catch (e: Exception) {
            }
        }
    }

    private fun removeFromFavorites(releaseId: Int) {
        viewModelScope.launch {
            try {
                libraryRepository.removeFromFavorites(releaseId)
            } catch (e: Exception) {
            }
        }
    }

}
