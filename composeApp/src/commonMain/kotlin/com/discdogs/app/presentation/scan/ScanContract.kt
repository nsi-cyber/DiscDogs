package com.discdogs.app.presentation.scan

import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController

data class ScanState(
    val isLoading: Boolean = false,
    val selectedScanType: ScanType = ScanType.BARCODE,
    val showImageScanningOption: Boolean = true,
    val permissionState: PermissionState = PermissionState.NotGranted,
)

sealed interface ScanEffect

sealed class ScanEvent {
    class ProvidePermission(val isFirstTime: Boolean) : ScanEvent()
    object OnBackClicked : ScanEvent()
    class OnSelectedScanTypeChanged(val scanType: ScanType) : ScanEvent()
    class OnPhotoCaptured(val imageBytes: ByteArray) : ScanEvent()
    class OnBarcodeCaptured(val barcode: String) : ScanEvent()
    class OnImageCaptured(val imageBytes: ByteArray) : ScanEvent()
    class SetPermissionController(val controller: PermissionsController) : ScanEvent()

}

enum class ScanType {
    BARCODE,
    IMAGE
}