package com.discdogs.app.core.camera

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFoundation.*
import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortraitUpsideDown
import platform.UIKit.UIViewController
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

/**
 * Enhanced UIViewController that manages camera preview, barcode scanning, and photo capture using AVCapturePhotoOutput.
 *
 * @property device The AVCaptureDevice to use for capturing video and photos.
 * @property onBarcodeSuccess A callback function that is invoked when barcodes are successfully detected.
 * @property onPhotoCaptured A callback function that is invoked when photos are captured.
 * @property isLoading Whether the camera is currently processing (prevents multiple captures).
 */
class EnhancedCameraViewController(
    private val device: AVCaptureDevice,
    private val isLoading: Boolean = false,
    private val onBarcodeSuccess: (String) -> Unit,
    private val onPhotoCaptured: (ByteArray) -> Unit,
) : UIViewController(null, null), AVCaptureMetadataOutputObjectsDelegateProtocol,
    AVCapturePhotoCaptureDelegateProtocol {

    private lateinit var captureSession: AVCaptureSession
    private lateinit var previewLayer: AVCaptureVideoPreviewLayer
    private lateinit var videoInput: AVCaptureDeviceInput
    private lateinit var photoOutput: AVCapturePhotoOutput
    private lateinit var metadataOutput: AVCaptureMetadataOutput

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.blackColor
        setupCamera()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupCamera() {
        captureSession = AVCaptureSession()

        // Configure session preset for high quality
        if (captureSession.canSetSessionPreset(AVCaptureSessionPresetPhoto)) {
            captureSession.setSessionPreset(AVCaptureSessionPresetPhoto)
        }

        try {
            videoInput =
                AVCaptureDeviceInput.deviceInputWithDevice(device, null) as AVCaptureDeviceInput
        } catch (e: Exception) {
            return
        }

        setupCaptureSession()
    }

    private fun setupCaptureSession() {
        // Add video input
        if (!captureSession.canAddInput(videoInput)) {
            return
        }
        captureSession.addInput(videoInput)

        // Add metadata output for barcode scanning
        metadataOutput = AVCaptureMetadataOutput()
        if (!captureSession.canAddOutput(metadataOutput)) {
            return
        }
        captureSession.addOutput(metadataOutput)
        setupMetadataOutput(metadataOutput)

        // Add photo output for high-quality photo capture
        photoOutput = AVCapturePhotoOutput()
        if (!captureSession.canAddOutput(photoOutput)) {
            return
        }
        captureSession.addOutput(photoOutput)
        setupPhotoOutput(photoOutput)

        setupPreviewLayer()
        captureSession.startRunning()
    }

    private fun setupMetadataOutput(metadataOutput: AVCaptureMetadataOutput) {
        metadataOutput.setMetadataObjectsDelegate(this, dispatch_get_main_queue())
        metadataOutput.metadataObjectTypes = listOf(AVMetadataObjectTypeEAN13Code)
    }

    private fun setupPhotoOutput(photoOutput: AVCapturePhotoOutput) {
        photoOutput.setHighResolutionCaptureEnabled(true)
        photoOutput.setLivePhotoCaptureEnabled(false)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupPreviewLayer() {
        previewLayer = AVCaptureVideoPreviewLayer.layerWithSession(captureSession)
        previewLayer.frame = view.layer.bounds
        previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
        view.layer.addSublayer(previewLayer)
        updatePreviewOrientation()
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        if (!captureSession.isRunning()) {
            captureSession.startRunning()
        }
    }

    override fun viewWillDisappear(animated: Boolean) {
        super.viewWillDisappear(animated)
        if (captureSession.isRunning()) {
            captureSession.stopRunning()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        previewLayer.frame = view.layer.bounds
        updatePreviewOrientation()
    }

    /**
     * Captures a high-quality photo using AVCapturePhotoOutput
     */
    fun capturePhoto() {
        if (isLoading) return

        val photoSettings = platform.AVFoundation.AVCapturePhotoSettings.photoSettings()

        photoOutput.setHighResolutionCaptureEnabled(true)
        photoSettings.flashMode = AVCaptureFlashModeOff


        // Set photo format
        if (photoOutput.availablePhotoCodecTypes.contains(AVVideoCodecTypeJPEG)) {
            //  photoSettings.format=AVVideoCodecTypeJPEG
            //  photoSettings.setFormat(AVVideoCodecTypeJPEG)
        }

        // Capture photo
        photoOutput.capturePhotoWithSettings(photoSettings, this)
    }

    /**
     * Sets the torch (flash) mode
     */
    @OptIn(ExperimentalForeignApi::class)
    fun setTorch(enabled: Boolean) {
        if (device.hasTorch) {
            try {
                device.lockForConfiguration(null)
                device.torchMode = if (enabled) AVCaptureTorchModeOn else AVCaptureTorchModeOff
                device.unlockForConfiguration()
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    private fun updatePreviewOrientation() {
        val orientation = UIApplication.sharedApplication.statusBarOrientation
        val videoOrientation = when (orientation) {
            UIInterfaceOrientationPortraitUpsideDown -> AVCaptureVideoOrientationPortraitUpsideDown
            UIInterfaceOrientationLandscapeLeft -> AVCaptureVideoOrientationLandscapeLeft
            UIInterfaceOrientationLandscapeRight -> AVCaptureVideoOrientationLandscapeRight
            else -> AVCaptureVideoOrientationPortrait
        }
        previewLayer.connection?.videoOrientation = videoOrientation
    }

    // MARK: - AVCaptureMetadataOutputObjectsDelegateProtocol
    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection,
    ) {
        if (isLoading) return
        processBarcodes(didOutputMetadataObjects)
    }

    private fun processBarcodes(metadataObjects: List<*>) {
        for (metadataObject in metadataObjects) {
            val barcodeObject = metadataObject as? AVMetadataMachineReadableCodeObject
            if (barcodeObject != null) {
                val barcodeString = barcodeObject.stringValue
                if (barcodeString != null) {
                    onBarcodeSuccess(barcodeString)
                    break
                }
            }
        }
    }

    // MARK: - AVCapturePhotoCaptureDelegateProtocol
    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: platform.Foundation.NSError?
    ) {
        if (error != null) {
            return
        }

        val imageData = didFinishProcessingPhoto.fileDataRepresentation()
        if (imageData != null) {
            val byteArray = imageData.toByteArray()
            onPhotoCaptured(byteArray)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun NSData.toByteArray(): ByteArray {
        return ByteArray(length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), bytes, length)
            }
        }
    }
}
