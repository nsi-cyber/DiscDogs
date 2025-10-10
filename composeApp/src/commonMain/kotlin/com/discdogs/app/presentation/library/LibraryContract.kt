package com.discdogs.app.presentation.library

import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.presentation.model.VinylResultUiModel

data class LibraryState(
    val isLoading: Boolean = false,
    val favoriteReleases: List<VinylResultUiModel>? = null,
    val selectedTab: LibraryTab = LibraryTab.FAVORITES,
    val lists: List<ReleaseList>? = null,
    val showCreateListDialog: Boolean = false
)

enum class LibraryTab {
    FAVORITES, LISTS
}

sealed interface LibraryEffect

sealed class LibraryEvent {
    object OnBackClicked : LibraryEvent()
    class OnReleaseDetail(val data: VinylResultUiModel) : LibraryEvent()
    class OnRemoveFromFavorites(val releaseId: Int) : LibraryEvent()
    object OnFavoritesTabSelected : LibraryEvent()
    object OnListsTabSelected : LibraryEvent()
    class OnListSelected(val listId: Long) : LibraryEvent()
    object OnCreateNewList : LibraryEvent()
    class OnCreateList(val name: String) : LibraryEvent()
    object OnDismissCreateListDialog : LibraryEvent()
    class OnDeleteList(val listId: Long) : LibraryEvent()
}