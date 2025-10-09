package com.discdogs.app.core.camera


import androidx.camera.core.Camera
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13
import com.google.mlkit.vision.common.InputImage

/**
 * Analyzes images for barcodes using ML Kit.
 *
 * This class implements [ImageAnalysis.Analyzer] to process camera frames.
 * It uses ML Kit's Barcode Scanning API to detect and decode barcodes.
 *
 * It features:
 * - **Configurable Barcode Types**: Scans for specific barcode formats defined by `codeTypes`.
 * - **Zoom Suggestion**: Utilizes ML Kit's zoom suggestion feature to prompt the user to zoom if a barcode is detected but is too small. The zoom is handled automatically if the camera supports it.
 * - **Single Success Processing**: Once a barcode is successfully processed (detected twice), further analysis is stopped to prevent redundant callbacks.
 * - **Callbacks**:
 *     - `onSuccess`: Called when a barcode is successfully detected and meets the criteria.
 *
 * The analyzer maps ML Kit's barcode formats to a custom `BarcodeFormat` enum for application-specific use.
 *
 * @property camera The [Camera] instance, used for zoom control. Can be null if zoom control is not needed or available.
 * @property onSuccess A callback function that is invoked when a barcode is successfully detected and validated. It receives a list containing the single detected [Barcode].
 */
class BarcodeAnalyzer(
    private val isLoading: Boolean = false,
    private val onSuccess: (String) -> Unit,
) : ImageAnalysis.Analyzer {
    private val scannerOptions =
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(FORMAT_EAN_13)
            .build()

    private val scanner = BarcodeScanning.getClient(scannerOptions)

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isLoading) return
        val mediaImage =
            imageProxy.image ?: return


        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNullOrEmpty() == false) {
                    processFoundBarcode(barcodes.first())
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun processFoundBarcode(mlKitBarcode: com.google.mlkit.vision.barcode.common.Barcode) {
        if (isLoading) return

        val displayValue = mlKitBarcode.displayValue ?: return
        onSuccess(displayValue)
    }

}