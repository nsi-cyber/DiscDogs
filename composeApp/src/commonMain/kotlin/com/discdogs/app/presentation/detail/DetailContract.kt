package com.discdogs.app.presentation.detail

import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.VinylDetailUiModel

data class DetailState(
    val isLoading: Boolean = false,
    val isPreviewLoading: Boolean = false,
    val releaseDetail: VinylDetailUiModel? = null,
    val playingItem: PlayingItem? = null,
    val backgroundImage: String? = null,
    val moreSheetVisible: Boolean = false,
    val barcodeSheetVisible: Boolean = false,
    val isFavorite: Boolean = false,


    val saveToListSheetVisible: Boolean = false,
    val lists: List<ReleaseList>? = null,
    val showCreateListDialog: Boolean = false,
    val releaseInLists: Set<Long> = emptySet()
)

data class PlayingItem(
    val id: String,
    val title: String,
    val image: String,
    val preview: String? = null,
    val progress: Float = 0f,
)

sealed interface DetailEffect {
    data object ShowMoreBottomSheet : DetailEffect
    data object DismissMoreBottomSheet : DetailEffect
    data object ShowBarcodeBottomSheet : DetailEffect
    data object DismissBarcodeBottomSheet : DetailEffect


    data object ShowSaveToListBottomSheet : DetailEffect
    data object DismissSaveToListBottomSheet : DetailEffect
}

sealed class DetailEvent {
    object OnBackClicked : DetailEvent()
    class OnLoadBackground(val image: String?) : DetailEvent()
    class OnPreviewTrack(val data: TrackListUiModel) : DetailEvent()
    class OnExternalWebsite(val type: ExternalWebsites) : DetailEvent()
    object OnReleaseTrack : DetailEvent()
    object OnShare : DetailEvent()
    object OnShowMoreBottomSheet : DetailEvent()
    object OnDismissMoreBottomSheet : DetailEvent()
    object OnShowBarcodeBottomSheet : DetailEvent()
    object OnDismissBarcodeBottomSheet : DetailEvent()
    object OnOtherReleases : DetailEvent()
    object OnToggleFavorite : DetailEvent()


    class OnLoadReleaseDetails(val releaseId: Int, val source: String = "UNKNOWN") : DetailEvent()
    object OnShowSaveToListBottomSheet : DetailEvent()
    object OnDismissSaveToListBottomSheet : DetailEvent()
    class OnAddToList(val listId: Long) : DetailEvent()
    object OnCreateNewList : DetailEvent()
    class OnCreateList(val name: String) : DetailEvent()
    object OnDismissCreateListDialog : DetailEvent()
}