package com.discdogs.app.presentation.releases

import com.discdogs.app.presentation.model.MastersVersionsUiModel

data class ReleasesState(
    val isLoading: Boolean = false,
    val resultList: List<MastersVersionsUiModel>? = null,

    )

sealed interface ReleasesEffect {
    data class ShowToast(val message: String) : ReleasesEffect
}

sealed class ReleasesEvent {
    object OnBackClicked : ReleasesEvent()
    class OnReleaseDetail(val data: MastersVersionsUiModel) : ReleasesEvent()

}
