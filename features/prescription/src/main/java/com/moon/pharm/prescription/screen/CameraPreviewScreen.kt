package com.moon.pharm.prescription.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moon.pharm.designsystem.component.button.PharmOutlinedButton
import com.moon.pharm.designsystem.component.button.PharmPrimaryButton
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.prescription.R
import com.moon.pharm.prescription.ocr.TextRecognitionAnalyzer
import com.moon.pharm.prescription.viewmodel.PrescriptionUiEvent
import com.moon.pharm.prescription.viewmodel.PrescriptionViewModel
import java.util.concurrent.Executors

@Composable
fun CameraPreviewRoute(
    viewModel: PrescriptionViewModel = hiltViewModel(),
    onNavigateToMedicationReview: (List<String>) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is PrescriptionUiEvent.NavigateToMedicationReview -> {
                    onNavigateToMedicationReview(event.scannedMedicationNames)
                }
            }
        }
    }

    CameraPreviewScreen(onTextRecognized = viewModel::onTextRecognized)
}

@Composable
fun CameraPreviewScreen(
    onTextRecognized: (String) -> Unit
) {
    if (LocalInspectionMode.current) {
        CameraPreviewPlaceholder()
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val cameraController = remember { LifecycleCameraController(context) }
    val captureExecutor = remember { Executors.newSingleThreadExecutor() }
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var recognizedText by remember { mutableStateOf("") }
    var isCameraActive by remember { mutableStateOf(true) }
    var isCapturing by remember { mutableStateOf(false) }
    var recognitionFailed by remember { mutableStateOf(false) }

    val textAnalyzer = remember {
        TextRecognitionAnalyzer(
            onTextFound = { text -> recognizedText = text },
            onFailure = { recognitionFailed = true },
            onComplete = {
                isCapturing = false
                recognitionFailed = recognitionFailed || recognizedText.isBlank()
            }
        )
    }

    LaunchedEffect(Unit) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraController.setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
    }

    DisposableEffect(lifecycleOwner, isCameraActive) {
        if (isCameraActive) {
            cameraController.bindToLifecycle(lifecycleOwner)
        }
        onDispose { cameraController.unbind() }
    }

    DisposableEffect(captureExecutor) {
        onDispose { captureExecutor.shutdown() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (previewBitmap == null) {
            CameraSurface(cameraController = cameraController)
        } else {
            Image(
                bitmap = previewBitmap!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.camera_preview_captured_image_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        CameraControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            hasCapturedImage = previewBitmap != null,
            isCapturing = isCapturing,
            recognizedText = recognizedText,
            recognitionFailed = recognitionFailed,
            onCapture = {
                isCapturing = true
                recognitionFailed = false
                cameraController.takePicture(
                    captureExecutor,
                    createCaptureCallback(
                        mainExecutor = mainExecutor,
                        onCaptured = { bitmap ->
                            previewBitmap = bitmap
                            isCameraActive = false
                        },
                        onImageCaptured = textAnalyzer::analyze,
                        onFailure = {
                            isCapturing = false
                            recognitionFailed = true
                        }
                    )
                )
            },
            onRetake = {
                previewBitmap = null
                recognizedText = ""
                recognitionFailed = false
                isCameraActive = true
            },
            onAnalyze = { onTextRecognized(recognizedText) }
        )
    }
}

@Composable
private fun CameraSurface(cameraController: LifecycleCameraController) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
                controller = cameraController
            }
        }
    )
}

@Composable
private fun CameraControls(
    modifier: Modifier,
    hasCapturedImage: Boolean,
    isCapturing: Boolean,
    recognizedText: String,
    recognitionFailed: Boolean,
    onCapture: () -> Unit,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!hasCapturedImage) {
            Text(
                text = stringResource(id = R.string.camera_preview_guide_title),
                color = PharmTheme.colors.surface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.camera_preview_security_notice),
                color = PharmTheme.colors.surface.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            PharmPrimaryButton(
                text = stringResource(id = R.string.camera_preview_btn_capture),
                onClick = onCapture,
                enabled = !isCapturing
            )
            return
        }

        when {
            isCapturing -> Text(
                text = stringResource(id = R.string.camera_preview_ocr_loading),
                color = PharmTheme.colors.surface,
                fontWeight = FontWeight.Bold
            )
            recognitionFailed -> Text(
                text = stringResource(id = R.string.camera_preview_ocr_failed),
                color = PharmTheme.colors.surface,
                textAlign = TextAlign.Center
            )
            else -> Text(
                text = stringResource(id = R.string.camera_preview_guide_analyze),
                color = PharmTheme.colors.surface,
                textAlign = TextAlign.Center
            )
        }

        if (recognizedText.isNotBlank()) {
            PharmPrimaryButton(
                text = stringResource(id = R.string.camera_preview_btn_analyze),
                onClick = onAnalyze,
                enabled = !isCapturing
            )
        }

        PharmOutlinedButton(onClick = onRetake) {
            Text(text = stringResource(id = R.string.camera_preview_btn_retake))
        }
    }
}

private fun createCaptureCallback(
    mainExecutor: java.util.concurrent.Executor,
    onCaptured: (Bitmap) -> Unit,
    onImageCaptured: (ImageProxy) -> Unit,
    onFailure: () -> Unit
): ImageCapture.OnImageCapturedCallback = object : ImageCapture.OnImageCapturedCallback() {
    override fun onCaptureSuccess(image: ImageProxy) {
        val previewBitmap = runCatching { image.toPreviewBitmap() }
            .getOrElse {
                image.close()
                mainExecutor.execute(onFailure)
                return
            }

        mainExecutor.execute { onCaptured(previewBitmap) }
        onImageCaptured(image)
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onError(exception: ImageCaptureException) {
        mainExecutor.execute(onFailure)
    }
}

private fun ImageProxy.toPreviewBitmap(): Bitmap {
    val imageBuffer = planes.first().buffer.duplicate()
    val imageBytes = ByteArray(imageBuffer.remaining())
    imageBuffer.get(imageBytes)
    val rotatedBitmap = checkNotNull(
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    ).rotate(imageInfo.rotationDegrees)
    val maxPreviewSize = 1_600
    val largestSide = maxOf(rotatedBitmap.width, rotatedBitmap.height)

    if (largestSide <= maxPreviewSize) return rotatedBitmap

    val scale = maxPreviewSize.toFloat() / largestSide
    return rotatedBitmap.scale(
        width = (rotatedBitmap.width * scale).toInt(),
        height = (rotatedBitmap.height * scale).toInt(),
        filter = true
    ).also { rotatedBitmap.recycle() }
}

private fun Bitmap.rotate(rotationDegrees: Int): Bitmap {
    if (rotationDegrees == 0) return this

    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        Matrix().apply { postRotate(rotationDegrees.toFloat()) },
        true
    ).also { recycle() }
}

@Composable
private fun CameraPreviewPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black)
    ) {
        Text(
            text = stringResource(id = R.string.camera_preview_guide_title),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            color = PharmTheme.colors.surface,
            fontWeight = FontWeight.Bold
        )
    }
}

@ThemePreviews
@Composable
private fun CameraPreviewScreenPreview() {
    PharmMasterTheme {
        CameraPreviewScreen(onTextRecognized = {})
    }
}
