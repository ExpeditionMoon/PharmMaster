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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asString
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.consult.screen.component.ConsultWriteContent
import com.moon.pharm.consult.viewmodel.ConsultViewModel

@Composable
fun ConsultWriteScreen(
    navController: NavController,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val writeState = uiState.writeState

    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = (userMessage as? ConsultUiMessage)?.asString()

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.updateImages(writeState.images + uri.toString())
        }
    }

    val isFormValid = writeState.title.isNotBlank() && writeState.content.isNotBlank()

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_write_title),
                    navigationType = TopBarNavigationType.Close,
                    onNavigationClick = { navController.popBackStack() }
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultWriteContent(
                title = writeState.title,
                content = writeState.content,
                images = writeState.images,
                isPublic = writeState.isPublic,
                isButtonEnabled = isFormValid,
                onTitleChange = viewModel::onTitleChanged,
                onContentChange = viewModel::onContentChanged,
                onImageRemove = { removedUrl ->
                    viewModel.updateImages(writeState.images.filter { it != removedUrl })
                },
                onVisibilityChange = viewModel::onVisibilityChanged,
                onCameraClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onNextClick = {
                    navController.navigate(ContentNavigationRoute.ConsultTabPharmacistScreen)
                }
            )
        }
    }
}