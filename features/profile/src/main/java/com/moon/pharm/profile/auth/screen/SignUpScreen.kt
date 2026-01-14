package com.moon.pharm.profile.auth.screen

import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.section.EmailPasswordSection
import com.moon.pharm.profile.auth.screen.section.NickNameSection
import com.moon.pharm.profile.auth.screen.section.PharmacistInfoSection
import com.moon.pharm.profile.auth.screen.section.UserTypeSection
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit, viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it.toString()) }
    }

    SignUpScreenContent(
        uiState = uiState,
        onUpdateUserType = { viewModel.updateUserType(it) },
        onUpdateEmail = { viewModel.updateEmail(it) },
        onCheckEmail = { viewModel.checkEmailDuplicate() },
        onUpdatePassword = { viewModel.updatePassword(it) },
        onUpdateNickName = { viewModel.updateNickName(it) },
        onUpdatePharmacyName = { viewModel.updatePharmacyName(it) },
        onUpdatePharmacistBio = { viewModel.updatePharmacistBio(it) },
        onImageClick = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onNextClick = { viewModel.moveToNextStep(onNavigateToHome) }
    )
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onUpdateUserType: (UserType) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onCheckEmail: () -> Unit,
    onUpdatePassword: (String) -> Unit,
    onUpdateNickName: (String) -> Unit,
    onUpdatePharmacyName: (String) -> Unit,
    onUpdatePharmacistBio: (String) -> Unit,
    onImageClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        containerColor = White,
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
                label = "SignUpStep"
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
                        onUpdateName = onUpdatePharmacyName,
                        onUpdateBio = onUpdatePharmacistBio
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))
        }
    }
}


@Preview(showBackground = true, name = "Step 1: Type")
@Composable
fun Step1Preview() {
    SignUpScreenContent(
        uiState = SignUpUiState(currentStep = SignUpStep.TYPE),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onUpdatePharmacyName = {},
        onUpdatePharmacistBio = {},
        onImageClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "Step 2: Email")
@Composable
fun Step2Preview() {
    SignUpScreenContent(
        uiState = SignUpUiState(
            currentStep = SignUpStep.EMAIL,
            email = "example@test.com",
            isEmailAvailable = true
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onUpdatePharmacyName = {},
        onUpdatePharmacistBio = {},
        onImageClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "Step 3: User NickName")
@Composable
fun Step3Preview() {
    SignUpScreenContent(
        uiState = SignUpUiState(
            currentStep = SignUpStep.NICKNAME,
            userType = UserType.GENERAL,
            nickName = "달리는약사",
            profileImageUri = null
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onUpdatePharmacyName = {},
        onUpdatePharmacistBio = {},
        onImageClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "Step 3: Pharmacist Mode")
@Composable
fun Step3PharmacistPreview() {
    SignUpScreenContent(
        uiState = SignUpUiState(
            currentStep = SignUpStep.NICKNAME,
            userType = UserType.PHARMACIST,
            nickName = "친절약사",
            profileImageUri = "https://example.com/image.jpg"
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onUpdatePharmacyName = {},
        onUpdatePharmacistBio = {},
        onImageClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "Step 4: Pharmacist Info")
@Composable
fun Step4Preview() {
    SignUpScreenContent(
        uiState = SignUpUiState(
            currentStep = SignUpStep.PHARMACIST_INFO,
            userType = UserType.PHARMACIST,
            pharmacyName = "행복약국",
            pharmacistBio = "안녕하세요"
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onUpdatePharmacyName = {},
        onUpdatePharmacistBio = {},
        onImageClick = {},
        onNextClick = {}
    )
}