package com.discdogs.app.core.camera

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.ByteArrayOutputStream

/**
 * Composable function that displays a camera preview for scanning barcodes.
 *
 * @param modifier The modifier to be applied to the scanner view.
 * @param isLoading A boolean indicating whether the scanner is currently loading.
 * @param scannerController An optional controller for managing scanner actions.
 * @param result A callback function that receives the barcode scanning result.
 * @param onPhotoCaptured A callback function that receives the captured photo as ByteArray.
 */
@Composable
actual fun ScannerView(
    modifier: Modifier,
    isLoading: Boolean,
    scannerController: ScannerController?,
    result: (String) -> Unit,
    onPhotoCaptured: ((ByteArray) -> Unit)?,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var initializationError: Throwable? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener(
            {
                try {
                    cameraProvider = future.get()
                } catch (e: Exception) {
                    initializationError = e
                }
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    var camera: Camera? by remember { mutableStateOf(null) }
    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    var torchEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(camera) {
        camera?.cameraInfo?.torchState?.observe(lifecycleOwner) {
            torchEnabled = it == TorchState.ON
        }
    }


    scannerController?.onTorchChange = { enabled ->
        if (!scannerController.externalFlashControl) {
            cameraControl?.enableTorch(enabled)
            scannerController.torchEnabled = enabled
        }
    }

    scannerController?.onPhotoCapture = {
        imageCapture?.let { capture ->
            capture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(imageProxy: androidx.camera.core.ImageProxy) {
                        try {
                            val bitmap = imageProxy.toBitmap()
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                            val byteArray = outputStream.toByteArray()
                            onPhotoCaptured?.invoke(byteArray)
                            outputStream.close()
                        } catch (e: Exception) {
                            // Handle error silently or log it
                        } finally {
                            imageProxy.close()
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        // Handle error silently or log it
                    }
                }
            )
        }
    }



    Box(modifier = modifier) {
        when {
            cameraProvider != null -> {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val preview = Preview.Builder().build()
                        val selector =
                            CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()

                        preview.surfaceProvider = previewView.surfaceProvider

                        val imageAnalysis =
                            ImageAnalysis.Builder()
                                .setTargetResolution(Size(1280, 720))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                        imageCapture = ImageCapture.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .build()

                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(ctx),
                            BarcodeAnalyzer(
                                isLoading = isLoading,
                                onSuccess = { scannedBarcode ->
                                    result(scannedBarcode)
                                },
                            ),
                        )

                        camera =
                            bindCamera(
                                lifecycleOwner = lifecycleOwner,
                                cameraProviderFuture = cameraProvider,
                                selector = selector,
                                preview = preview,
                                imageAnalysis = imageAnalysis,
                                imageCapture = imageCapture,
                                cameraControl = { cameraControl = it },
                            )

                        previewView
                    },
                    onRelease = {
                        cameraProvider?.unbindAll()
                    },
                )
            }

            else -> {}
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            camera = null
            cameraControl = null
        }
    }
}

internal fun bindCamera(
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ProcessCameraProvider?,
    selector: CameraSelector,
    preview: Preview,
    imageAnalysis: ImageAnalysis,
    imageCapture: ImageCapture?,
    cameraControl: (CameraControl?) -> Unit,
): Camera? {
    return runCatching {
        cameraProviderFuture?.unbindAll()
        cameraProviderFuture?.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageAnalysis,
            imageCapture,
        ).also {
            cameraControl(it?.cameraControl)
        }
    }.getOrElse {
        null
    }
}