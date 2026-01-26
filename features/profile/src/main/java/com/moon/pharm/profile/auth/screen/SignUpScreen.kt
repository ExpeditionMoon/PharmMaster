package com.moon.pharm.profile.auth.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.auth.mapper.asString
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.model.SignUpUiMessage
import com.moon.pharm.profile.auth.screen.component.PharmacySearchOverlay
import com.moon.pharm.profile.auth.screen.component.SignUpHeader
import com.moon.pharm.profile.auth.screen.section.EmailPasswordSection
import com.moon.pharm.profile.auth.screen.section.NickNameSection
import com.moon.pharm.profile.auth.screen.section.PharmacistInfoSection
import com.moon.pharm.profile.auth.screen.section.SignUpButtonSection
import com.moon.pharm.profile.auth.screen.section.UserTypeSection
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

private const val ANIMATION_LABEL_STEP = "SignUpStep"

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit, viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPharmacySearch by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = (userMessage as? SignUpUiMessage)?.asString()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it.toString()) }
    }

    if (showPharmacySearch) {
        PharmacySearchOverlay(
            uiState = uiState,
            viewModel = viewModel,
            onClose = { showPharmacySearch = false }
        )
    } else {
        SignUpScreenContent(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onUpdateUserType = { viewModel.updateUserType(it) },
            onUpdateEmail = { viewModel.updateEmail(it) },
            onCheckEmail = { viewModel.checkEmailDuplicate() },
            onUpdatePassword = { viewModel.updatePassword(it) },
            onUpdateNickName = { viewModel.updateNickName(it) },
            onSearchPharmacyClick = { showPharmacySearch = true },
            onUpdatePharmacistBio = { viewModel.updatePharmacistBio(it) },
            onImageClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onNextClick = { viewModel.moveToNextStep() }
        )
    }
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    snackbarHostState: SnackbarHostState,
    onUpdateUserType: (UserType) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onCheckEmail: () -> Unit,
    onUpdatePassword: (String) -> Unit,
    onUpdateNickName: (String) -> Unit,
    onSearchPharmacyClick: () -> Unit,
    onUpdatePharmacistBio: (String) -> Unit,
    onImageClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        containerColor = White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            SignUpHeader()

            Spacer(modifier = Modifier.weight(0.8f))

            AnimatedContent(
                targetState = uiState.currentStep,
                label = ANIMATION_LABEL_STEP
            ) { step ->
                when (step) {
                    SignUpStep.TYPE -> UserTypeSection(uiState.userType, onUpdateUserType)
                    SignUpStep.EMAIL -> EmailPasswordSection(
                        email = uiState.email,
                        password = uiState.password,
                        isAvailable = uiState.isEmailAvailable,
                        isEmailChecking = uiState.isEmailChecking,
                        onUpdateEmail = onUpdateEmail,
                        onUpdatePassword = onUpdatePassword,
                        onCheckClick = onCheckEmail
                    )

                    SignUpStep.NICKNAME -> NickNameSection(
                        nickName = uiState.nickName,
                        profileImageUri = uiState.profileImageUri,
                        onImageClick = onImageClick,
                        onNickNameChange = onUpdateNickName
                    )

                    SignUpStep.PHARMACIST_INFO -> PharmacistInfoSection(
                        pharmacyName = uiState.pharmacyName,
                        bio = uiState.pharmacistBio,
                        onSearchClick = onSearchPharmacyClick,
                        onUpdateBio = onUpdatePharmacistBio
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1.2f))

            SignUpButtonSection(
                uiState = uiState,
                onNextClick = onNextClick
            )
        }
    }
}