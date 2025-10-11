package com.discdogs.app.core.camera


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.defaultDeviceWithDeviceType
import platform.AVFoundation.hasTorch
import platform.AVFoundation.torchMode

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ScannerView(
    modifier: Modifier,
    isLoading: Boolean,
    scannerController: ScannerController?,
    result: (String) -> Unit,
    onPhotoCaptured: ((ByteArray) -> Unit)?,
) {
    var torchEnabled by remember { mutableStateOf(false) }

    var cameraViewController by remember { mutableStateOf<EnhancedCameraViewController?>(null) }
    val captureDevice: AVCaptureDevice? =
        remember {
            AVCaptureDevice.defaultDeviceWithDeviceType(
                AVCaptureDeviceTypeBuiltInWideAngleCamera,
                AVMediaTypeVideo,
                AVCaptureDevicePositionBack,
            )
        }

    if (captureDevice == null) {
        return
    }

    scannerController?.onTorchChange = { enabled ->
        if (!scannerController.externalFlashControl) {
            runCatching {
                if (captureDevice.hasTorch) {
                    captureDevice.lockForConfiguration(null)
                    captureDevice.torchMode =
                        if (enabled) AVCaptureTorchModeOn else AVCaptureTorchModeOff
                    captureDevice.unlockForConfiguration()
                    torchEnabled = enabled
                    scannerController.torchEnabled = enabled
                }
            }
        }
    }

    scannerController?.onPhotoCapture = {
        cameraViewController?.capturePhoto()
    }




    cameraViewController =
        remember {
            EnhancedCameraViewController(
                device = captureDevice,
                isLoading = isLoading,
                onBarcodeSuccess = { scannedBarcode ->
                    result(scannedBarcode)
                },
                onPhotoCaptured = { byteArray ->
                    onPhotoCaptured?.invoke(byteArray)
                }
            )
        }

    Box(modifier = modifier) {
        UIKitViewController(
            factory = { cameraViewController!! },
            modifier = Modifier.fillMaxSize(),
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraViewController = null
        }
    }
}