package com.discdogs.app.core.camera

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoOrientation
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeLeft
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeRight
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoOrientationPortraitUpsideDown
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerCameraDevice
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIInterfaceOrientation
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortraitUpsideDown
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

/**
 * A UIViewController that manages the camera preview and barcode scanning.
 *
 * @property device The AVCaptureDevice to use for capturing video.
 * @property onBarcodeSuccess A callback function that is invoked when barcodes are successfully detected.
 * @property filter A callback function that is invoked when barcode result is processed. [onBarcodeSuccess] will only be invoked
 * if the invocation of this property resolves to true.
 */
class CameraViewController(
    private val device: AVCaptureDevice,
    private val isLoading: Boolean = false,
    private val onBarcodeSuccess: (String) -> Unit,
) : UIViewController(null, null), AVCaptureMetadataOutputObjectsDelegateProtocol,
    UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
    private lateinit var captureSession: AVCaptureSession
    private lateinit var previewLayer: AVCaptureVideoPreviewLayer
    private lateinit var videoInput: AVCaptureDeviceInput
    private var photoCaptureCallback: ((ByteArray) -> Unit)? = null


    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.blackColor
        setupCamera()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupCamera() {
        captureSession = AVCaptureSession()

        try {
            videoInput =
                AVCaptureDeviceInput.deviceInputWithDevice(device, null) as AVCaptureDeviceInput
        } catch (e: Exception) {
            return
        }

        setupCaptureSession()
    }

    private fun setupCaptureSession() {
        val metadataOutput = AVCaptureMetadataOutput()

        if (!captureSession.canAddInput(videoInput)) {
            return
        }
        captureSession.addInput(videoInput)

        if (!captureSession.canAddOutput(metadataOutput)) {
            return
        }
        captureSession.addOutput(metadataOutput)

        setupMetadataOutput(metadataOutput)
        setupPreviewLayer()

        captureSession.startRunning()
    }

    private fun setupMetadataOutput(metadataOutput: AVCaptureMetadataOutput) {
        metadataOutput.setMetadataObjectsDelegate(this, dispatch_get_main_queue())
        metadataOutput.metadataObjectTypes = listOf(AVMetadataObjectTypeEAN13Code)
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

    fun capturePhoto(callback: (ByteArray) -> Unit) {
        photoCaptureCallback = callback

        val imagePickerController = UIImagePickerController()
        imagePickerController.delegate = this
        imagePickerController.sourceType =
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        imagePickerController.cameraDevice =
            UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear
        imagePickerController.cameraCaptureMode =
            UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto

        presentViewController(imagePickerController, animated = true, completion = null)
    }

    @OptIn(ExperimentalForeignApi::class)
    fun NSData.toByteArray(): ByteArray {
        return ByteArray(length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), bytes, length)
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)
        val image =
            didFinishPickingMediaWithInfo[platform.UIKit.UIImagePickerControllerOriginalImage] as? UIImage
        if (image != null) {
            val imageData: NSData? = UIImageJPEGRepresentation(image, 1.0)
            imageData?.toByteArray()?.let {
                photoCaptureCallback?.invoke(it)
            }
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection,
    ) {
        if (isLoading) return
        processBarcodes(didOutputMetadataObjects)
    }

    private fun processBarcodes(metadataObjects: List<*>) {
        metadataObjects
            .filterIsInstance<AVMetadataMachineReadableCodeObject>()
            .mapNotNull { metadataObject ->
                if (!::previewLayer.isInitialized) return@mapNotNull null
                previewLayer.transformedMetadataObjectForMetadataObject(metadataObject)
                        as? AVMetadataMachineReadableCodeObject
            }
            .filter { barcodeObject ->
                AVMetadataObjectTypeEAN13Code == barcodeObject.type
            }.forEach { barcodeObject ->
                barcodeObject.stringValue?.let { value ->
                    onBarcodeSuccess(value)
                }

            }
    }


    private fun updatePreviewOrientation() {
        if (!::previewLayer.isInitialized) return

        val connection = previewLayer.connection ?: return

        val uiOrientation: UIInterfaceOrientation =
            UIApplication.sharedApplication().statusBarOrientation

        val videoOrientation: AVCaptureVideoOrientation =
            when (uiOrientation) {
                UIInterfaceOrientationLandscapeLeft -> AVCaptureVideoOrientationLandscapeLeft
                UIInterfaceOrientationLandscapeRight -> AVCaptureVideoOrientationLandscapeRight
                UIInterfaceOrientationPortraitUpsideDown -> AVCaptureVideoOrientationPortraitUpsideDown
                else -> AVCaptureVideoOrientationPortrait
            }

        connection.videoOrientation = videoOrientation
    }

}