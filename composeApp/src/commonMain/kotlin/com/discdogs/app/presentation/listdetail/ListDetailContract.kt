package com.discdogs.app.presentation.listdetail

import com.discdogs.app.presentation.model.VinylResultUiModel


data class ListDetailState(
    val isLoading: Boolean = false,
    val listId: Long? = null,
    val listName: String? = null,
    val releases: List<VinylResultUiModel>? = null
)

sealed interface ListDetailEffect {
    data class NavigateToReleaseDetail(val releaseId: Int, val image: String) : ListDetailEffect
    data class ShowToast(val message: String) : ListDetailEffect
}

sealed class ListDetailEvent {
    object OnBackClicked : ListDetailEvent()
    class OnReleaseClick(val release: VinylResultUiModel) : ListDetailEvent()
    class OnRemoveRelease(val releaseId: Int) : ListDetailEvent()
}


