package com.moon.pharm.consult.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.component.input.PrimaryTextField
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.component.toggle.CustomSwitch
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.consult.R
import com.moon.pharm.consult.common.ConsultUiMessage
import com.moon.pharm.consult.common.asString
import com.moon.pharm.consult.screen.section.PhotoAttachmentSection
import com.moon.pharm.consult.viewmodel.ConsultViewModel

@Composable
fun ConsultWriteScreen(
    navController : NavController,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val writeState = uiState.writeState

    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = (userMessage as? ConsultUiMessage)?.asString()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val currentImages = writeState.images
            viewModel.updateImages(currentImages + uri.toString())
        }
    }

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    val isFormValid = writeState.title.isNotBlank() && writeState.content.isNotBlank()

    Scaffold(
        containerColor = backgroundLight,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(
                    snackbarData = data, type = SnackbarType.ERROR
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultWriteContent(
                title = writeState.title,
                content = writeState.content,
                images = writeState.images,
                isPublic = writeState.isPublic,
                isButtonEnabled = isFormValid,

                onTitleChange = { viewModel.onTitleChanged(it) },
                onContentChange = { viewModel.onContentChanged(it) },
                onImageChange = { viewModel.updateImages(it) },
                onVisibilityChange = { viewModel.onVisibilityChanged(it) },
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

@Composable
fun ConsultWriteContent(
    title: String,
    content: String,
    images: List<String>,
    isPublic: Boolean,
    isButtonEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onImageChange: (List<String>) -> Unit,
    onCameraClick: () -> Unit = {},
    onVisibilityChange: (Boolean) -> Unit,
    onNextClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .imePadding()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp, bottom = 10.dp)
    ) {
        PrimaryTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = stringResource(R.string.consult_write_placeholder_title),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = stringResource(R.string.consult_write_placeholder_content),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = primaryLight,
                lineHeight = 24.sp
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PhotoAttachmentSection(
            images = images,
            onCameraClick = onCameraClick,
            onRemoveImage = { imageUrl ->
                onImageChange(images.filter { it != imageUrl })
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.consult_write_public_setting_title),
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = if (isPublic) stringResource(R.string.consult_write_public_desc_all)
                        else stringResource(R.string.consult_write_public_desc_private),
                        fontSize = 12.sp,
                        color = SecondFont
                    )
                }
                CustomSwitch(
                    checked = isPublic,
                    onCheckedChange = onVisibilityChange
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                enabled = isButtonEnabled
            ) {
                Text(stringResource(R.string.consult_button_next))
            }
        }
    }
}