package com.discdogs.app.presentation.masterDetail

import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.VinylDetailUiModel

data class MasterDetailState(
    val isLoading: Boolean = false,
    val isPreviewLoading: Boolean = false,
    val masterDetail: VinylDetailUiModel? = null,
    val playingItem: PlayingItem? = null,
    val backgroundImage: String? = null,
    val moreSheetVisible: Boolean = false,
)

data class PlayingItem(
    val id: String,
    val title: String,
    val image: String,
    val preview: String? = null,
    val progress: Float = 0f,
)

sealed interface MasterDetailEffect {
    data object ShowMoreBottomSheet : MasterDetailEffect
    data object DismissMoreBottomSheet : MasterDetailEffect
}

sealed class MasterDetailEvent {
    object OnBackClicked : MasterDetailEvent()
    class OnLoadBackground(val image: String?) : MasterDetailEvent()
    class OnPreviewTrack(val data: TrackListUiModel) : MasterDetailEvent()
    class OnExternalWebsite(val type: ExternalWebsites) : MasterDetailEvent()
    object OnReleaseTrack : MasterDetailEvent()
    object OnShare : MasterDetailEvent()
    object OnShowMoreBottomSheet : MasterDetailEvent()
    object OnDismissMoreBottomSheet : MasterDetailEvent()
    object OnOtherReleases : MasterDetailEvent()


    class OnLoadMasterDetails(val masterId: Int, val source: String = "UNKNOWN") :
        MasterDetailEvent()

}