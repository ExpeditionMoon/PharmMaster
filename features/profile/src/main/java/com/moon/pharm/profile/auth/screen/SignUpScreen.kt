package com.moon.pharm.profile.auth.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.component.map.PharmacySelector
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.common.SignUpUiMessage
import com.moon.pharm.profile.auth.mapper.asString
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.section.EmailPasswordSection
import com.moon.pharm.profile.auth.screen.section.NickNameSection
import com.moon.pharm.profile.auth.screen.section.PharmacistInfoSection
import com.moon.pharm.profile.auth.screen.section.UserTypeSection
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

private const val ANIMATION_LABEL_STEP = "SignUpStep"

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit, viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPharmacySearch by remember { mutableStateOf(false) }
    var tempSelectedPharmacy by remember { mutableStateOf<Pharmacy?>(null) }
    var isLocationGranted by remember { mutableStateOf(false) }

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

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL),
            15f
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it.toString()) }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        isLocationGranted = isGranted

        if (isGranted) {
            viewModel.fetchCurrentLocationAndSearch()
        } else {
            viewModel.fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
        }
    }

    if (showPharmacySearch) {
        LaunchedEffect(Unit) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        PharmacySelector(
            pharmacies = uiState.pharmacySearchResults,
            selectedPharmacy = tempSelectedPharmacy,
            isLocationEnabled = isLocationGranted,
            onPharmacyClick = { pharmacy -> tempSelectedPharmacy = pharmacy },
            cameraPositionState = cameraPositionState,
            cameraMoveEvent = viewModel.moveCameraEvent,
            onSearch = { query -> viewModel.searchPharmacies(query) },
            onSearchArea = { lat, lng -> viewModel.fetchNearbyPharmacies(lat, lng) },
            onBackClick = {
                viewModel.clearSearchResults()
                showPharmacySearch = false
            },
            bottomContent = {
                if (tempSelectedPharmacy != null) {
                    Button(
                        onClick = {
                            viewModel.updatePharmacy(tempSelectedPharmacy!!)
                            viewModel.clearSearchResults()
                            showPharmacySearch = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                            .padding(bottom = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.signup_pharmacy_selected_format,
                                tempSelectedPharmacy?.name.orEmpty()
                            ),
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = White
                        )
                    }
                }
            }
        )

        BackHandler {
            viewModel.clearSearchResults()
            showPharmacySearch = false
        }
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
        bottomBar = {
            val buttonText = when (uiState.currentStep) {
                SignUpStep.TYPE -> stringResource(R.string.signup_button_next)
                SignUpStep.EMAIL -> stringResource(R.string.signup_button_next)
                SignUpStep.NICKNAME ->
                    if (uiState.userType == UserType.PHARMACIST) stringResource(R.string.signup_button_next)
                    else stringResource(R.string.signup_button_complete)
                SignUpStep.PHARMACIST_INFO -> stringResource(R.string.signup_button_complete)
            }

            val isNextEnabled = !uiState.isLoading && when (uiState.currentStep) {
                SignUpStep.TYPE -> true
                SignUpStep.EMAIL -> (uiState.isEmailAvailable == true) && (uiState.password.length >= 6)
                SignUpStep.NICKNAME -> uiState.nickName.isNotBlank()
                SignUpStep.PHARMACIST_INFO -> uiState.pharmacyName.isNotBlank() && uiState.pharmacistBio.isNotBlank()
            }

            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                enabled = isNextEnabled
            ) {
                if (uiState.isLoading) {
                    CircularProgressBar()
                } else {
                    Text(text = buttonText, fontSize = 16.sp, color = White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(text = stringResource(R.string.signup_button_complete), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Primary)

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
        }
    }
}