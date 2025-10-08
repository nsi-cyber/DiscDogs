package com.discdogs.app.presentation.search

import com.discdogs.app.presentation.model.VinylResultUiModel


data class SearchState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val resultList: List<VinylResultUiModel>? = null,
    val recentSearchedReleases: List<VinylResultUiModel>? = null,
    val recentScannedReleases: List<VinylResultUiModel>? = null,
)

sealed interface SearchEffect {
    data class ShowToast(val message: String) : SearchEffect
}

sealed class SearchEvent {
    object OnBackClicked : SearchEvent()
    object OnClearQuery : SearchEvent()
    class OnSearchQuery(val query: String) : SearchEvent()
    class OnReleaseDetail(val data: VinylResultUiModel) : SearchEvent()
    class OnRecentSearchedReleaseClick(val release: VinylResultUiModel) : SearchEvent()
    class OnRecentScannedReleaseClick(val release: VinylResultUiModel) : SearchEvent()
}