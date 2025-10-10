package com.discdogs.app.core.camera


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Controller for managing scanner functionalities like torch, zoom, and photo capture.
 *
 * This class provides a way to control the scanner's torch (flash), zoom level, and photo capture.
 * It uses mutable state properties that can be observed for changes, allowing UI
 * updates in response to scanner state modifications.
 *
 * @property torchEnabled A boolean indicating whether the torch is currently enabled.
 *                        Defaults to `false`. Can be observed for changes.
 * @property externalFlashControl A boolean indicating whether external flash control is enabled.
 *                                When true, the flash state is controlled externally rather than internally.
 *                                Defaults to `false`.
 */
class ScannerController {
    var torchEnabled by mutableStateOf(false)
    var externalFlashControl by mutableStateOf(false)

    internal var onTorchChange: ((Boolean) -> Unit)? = null
    internal var onPhotoCapture: (() -> Unit)? = null

    fun setTorch(enabled: Boolean) {
        onTorchChange?.invoke(enabled)
    }

    fun capturePhoto() {
        onPhotoCapture?.invoke()
    }
}