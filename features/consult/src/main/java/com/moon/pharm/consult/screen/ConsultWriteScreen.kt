package com.moon.pharm.consult.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asConsultString
import com.moon.pharm.consult.mapper.toConsultSnackbarType
import com.moon.pharm.consult.screen.component.ConsultWriteContent
import com.moon.pharm.consult.viewmodel.ConsultWriteEffect
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel

@Composable
fun ConsultWriteScreen(
    viewModel: ConsultWriteViewModel,
    onNavigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToPharmacist: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var currentSnackbarType by remember { mutableStateOf(SnackbarType.INFO) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConsultWriteEffect.ShowMessage -> {
                    currentSnackbarType = effect.message.toConsultSnackbarType()
                    snackbarHostState.showSnackbar(effect.message.asConsultString(context))
                }
                is ConsultWriteEffect.MoveToPharmacist -> {
                    onNavigateToPharmacist()
                }
                is ConsultWriteEffect.UpdateSuccess -> {
                    onNavigateUp()
                }
                is ConsultWriteEffect.CreateSuccess,
                is ConsultWriteEffect.MoveCamera -> Unit
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.updateImages(uiState.images + uri.toString())
        }
    }

    val isFormValid = uiState.title.isNotBlank() && uiState.content.isNotBlank()

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_write_title),
                    navigationType = TopBarNavigationType.Close,
                    onNavigationClick = onNavigateBack
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = currentSnackbarType)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultWriteContent(
                title = uiState.title,
                content = uiState.content,
                images = uiState.images,
                isPublic = uiState.isPublic,
                isButtonEnabled = isFormValid,
                isEditMode = uiState.isEditMode,
                onTitleChange = viewModel::onTitleChanged,
                onContentChange = viewModel::onContentChanged,
                onImageRemove = { removedUrl ->
                    viewModel.updateImages(uiState.images.filter { it != removedUrl })
                },
                onVisibilityChange = viewModel::onVisibilityChanged,
                onCameraClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onNextClick = {
                    viewModel.onNextClick()
                }
            )
        }
    }
}
