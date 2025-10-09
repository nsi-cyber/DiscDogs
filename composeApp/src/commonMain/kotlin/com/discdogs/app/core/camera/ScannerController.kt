package com.discdogs.app.core.camera


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Controller for managing scanner functionalities like torch and zoom.
 *
 * This class provides a way to control the scanner's torch (flash) and zoom level.
 * It uses mutable state properties that can be observed for changes, allowing UI
 * updates in response to scanner state modifications.
 *
 * @property torchEnabled A boolean indicating whether the torch is currently enabled.
 *                        Defaults to `false`. Can be observed for changes.
 */
class ScannerController {
    var torchEnabled by mutableStateOf(false)


    internal var onTorchChange: ((Boolean) -> Unit)? = null

    fun setTorch(enabled: Boolean) {
        onTorchChange?.invoke(enabled)
    }

}