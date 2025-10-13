package com.discdogs.app.presentation.releases

import com.discdogs.app.presentation.model.MastersVersionsUiModel

data class ReleasesState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val resultList: List<MastersVersionsUiModel>? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val totalPages: Int? = null,
    val masterId: Int? = null,
)

sealed interface ReleasesEffect {
    data class ShowToast(val message: String) : ReleasesEffect
}

sealed class ReleasesEvent {
    object OnBackClicked : ReleasesEvent()
    object LoadMore : ReleasesEvent()
    class OnReleaseDetail(val data: MastersVersionsUiModel) : ReleasesEvent()
}
