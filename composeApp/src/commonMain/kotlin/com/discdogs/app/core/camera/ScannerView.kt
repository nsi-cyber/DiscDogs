package com.discdogs.app.core.camera


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable function that displays a scanner view for scanning barcodes.
 *
 * @param modifier The modifier to be applied to the scanner view.
 * @param scannerController An optional controller for controlling the scanner.
 * @param result A callback function that is invoked when a barcode is scanned.
 * @param onPhotoCaptured A callback function that is invoked when a photo is captured, providing the image as ByteArray.
 */
@Composable
expect fun ScannerView(
    modifier: Modifier = Modifier.fillMaxSize(),
    isLoading: Boolean = false,
    scannerController: ScannerController? = null,
    result: (String) -> Unit,
    onPhotoCaptured: ((ByteArray) -> Unit)? = null,
)