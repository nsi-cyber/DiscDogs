package com.discdogs.app.presentation.scan

data class ScanState(
    val isLoading: Boolean = false,
    val selectedScanType: ScanType = ScanType.BARCODE,
    val showImageScanningOption: Boolean = true
)

sealed interface ScanEffect

sealed class ScanEvent {
    object OnBackClicked : ScanEvent()
    class OnSelectedScanTypeChanged(val scanType: ScanType) : ScanEvent()
//    class OnPhotoCaptured(val bitmap: Bitmap) : ScanEvent()
    //   class OnImageCaptured(val imageProxy: ImageProxy) : ScanEvent()

}

enum class ScanType {
    BARCODE,
    IMAGE
}