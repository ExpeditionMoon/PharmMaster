package com.moon.pharm.prescription.screen

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.prescription.ocr.TextRecognitionAnalyzer
import com.moon.pharm.prescription.viewmodel.PrescriptionUiEvent
import com.moon.pharm.prescription.viewmodel.PrescriptionViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CameraPreviewRoute(
    viewModel: PrescriptionViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when(event) {
                is PrescriptionUiEvent.NavigateToCreate -> {
                    navController?.navigate(
                        ContentNavigationRoute.MedicationTabCreateScreen(
                            scannedList = event.scannedList
                        )
                    ) {
                        popUpTo(ContentNavigationRoute.PrescriptionCapture) { inclusive = false }
                    }
                }
            }
        }
    }

    CameraPreviewScreen(
        onTextRecognized = viewModel::onTextRecognized
    )
}

@Composable
fun CameraPreviewScreen(
    onTextRecognized: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    LaunchedEffect(Unit) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraController.setEnabledUseCases(
            LifecycleCameraController.IMAGE_ANALYSIS or LifecycleCameraController.IMAGE_CAPTURE
        )

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer { text ->
                onTextRecognized(text)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    controller = cameraController
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )

        DisposableEffect(lifecycleOwner) {
            cameraController.bindToLifecycle(lifecycleOwner)
            onDispose {
                cameraController.unbind()
            }
        }

        Text(
            text = "약 봉투나 처방전을 비춰주세요",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            color = White
        )
    }
}