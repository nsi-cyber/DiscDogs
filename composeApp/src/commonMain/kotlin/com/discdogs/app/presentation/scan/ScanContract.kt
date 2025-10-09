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
    class OnPhotoCaptured(val imageBytes: ByteArray) : ScanEvent()
    class OnBarcodeCaptured(val barcode: String) : ScanEvent()
    class OnImageCaptured(val imageBytes: ByteArray) : ScanEvent()

}

enum class ScanType {
    BARCODE,
    IMAGE
}