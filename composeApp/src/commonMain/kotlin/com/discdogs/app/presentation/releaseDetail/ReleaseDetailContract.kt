package com.discdogs.app.presentation.releaseDetail

import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.PlayingItem
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.VinylDetailUiModel

data class ReleaseDetailState(
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


sealed interface ReleaseDetailEffect {
    data object ShowMoreBottomSheet : ReleaseDetailEffect
    data object DismissMoreBottomSheet : ReleaseDetailEffect
    data object ShowBarcodeBottomSheet : ReleaseDetailEffect
    data object DismissBarcodeBottomSheet : ReleaseDetailEffect


    data object ShowSaveToListBottomSheet : ReleaseDetailEffect
    data object DismissSaveToListBottomSheet : ReleaseDetailEffect
}

sealed class ReleaseDetailEvent {
    object OnBackClicked : ReleaseDetailEvent()
    class OnLoadBackground(val image: String?) : ReleaseDetailEvent()
    class OnPreviewTrack(val data: TrackListUiModel) : ReleaseDetailEvent()
    class OnExternalWebsite(val type: ExternalWebsites) : ReleaseDetailEvent()
    object OnReleaseTrack : ReleaseDetailEvent()
    object OnShare : ReleaseDetailEvent()
    object OnShowMoreBottomSheet : ReleaseDetailEvent()
    object OnDismissMoreBottomSheet : ReleaseDetailEvent()
    object OnShowBarcodeBottomSheet : ReleaseDetailEvent()
    object OnDismissBarcodeBottomSheet : ReleaseDetailEvent()
    object OnOtherReleases : ReleaseDetailEvent()
    object OnToggleFavorite : ReleaseDetailEvent()


    class OnLoadReleaseDetails(val releaseId: Int, val source: String = "UNKNOWN") :
        ReleaseDetailEvent()

    object OnShowSaveToListBottomSheet : ReleaseDetailEvent()
    object OnDismissSaveToListBottomSheet : ReleaseDetailEvent()
    class OnAddToList(val listId: Long) : ReleaseDetailEvent()
    object OnCreateNewList : ReleaseDetailEvent()
    class OnCreateList(val name: String) : ReleaseDetailEvent()
    object OnDismissCreateListDialog : ReleaseDetailEvent()
}