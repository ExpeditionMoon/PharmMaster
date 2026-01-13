package com.moon.pharm.profile.auth.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit, viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
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
        onImageClick = { galleryLauncher.launch("image/*") },
        onNextClick = { viewModel.moveToNextStep(onNavigateToHome) }
    )
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onUpdateUserType: (String) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onCheckEmail: () -> Unit,
    onUpdatePassword: (String) -> Unit,
    onUpdateNickName: (String) -> Unit,
    onImageClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        containerColor = White,
        bottomBar = {
            val buttonText = when (uiState.currentStep) {
                SignUpStep.TYPE -> "다음"
                SignUpStep.EMAIL -> "다음"
                SignUpStep.NICKNAME -> "회원가입"
            }

            val isNextEnabled = !uiState.isLoading && when (uiState.currentStep) {
                SignUpStep.TYPE -> true
                SignUpStep.EMAIL -> (uiState.isEmailAvailable == true) && (uiState.password.length >= 6)
                SignUpStep.NICKNAME -> uiState.nickName.isNotBlank()
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

            Text(text = "회원가입", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Primary)

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
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))
        }
    }
}

@Composable
fun UserTypeSection(selectedType: String, onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UserTypeRow(label = "일반", isSelected = selectedType == "일반", onSelect = { onSelect("일반") })

        Spacer(modifier = Modifier.height(10.dp))

        UserTypeRow(label = "약사", isSelected = selectedType == "약사", onSelect = { onSelect("약사") })
    }
}

@Composable
fun UserTypeRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .selectable(
                selected = isSelected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .border(1.dp, SecondFont, RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 16.sp
        )
        RadioButton(
            selected = isSelected,
            onClick = null
        )
    }
}

@Composable
fun EmailPasswordSection(
    email: String,
    password: String,
    isAvailable: Boolean?,
    isEmailChecking: Boolean,
    onUpdateEmail: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onCheckClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = onUpdateEmail,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter Your Email") },
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                Box (
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = if (isEmailChecking || isAvailable == true) Color.Gray else Primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable(enabled = !isEmailChecking && isAvailable != true) {
                            onCheckClick()
                        }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isEmailChecking) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isAvailable == true) "확인 완료" else "중복 확인",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }
            }
        )
        if (isAvailable == true) {
            Text("사용 가능한 이메일입니다.", color = Primary, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
        } else if (isAvailable == false) {
            Text("이미 등록된 이메일입니다.", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
        }

        // 2. 비밀번호 입력창 (이메일 확인 완료 시에만 등장)
        if (isAvailable == true) {
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onUpdatePassword,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("비밀번호 (6자리 이상)") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Password
                )
            )
        }
    }
}

@Composable
fun NickNameSection(
    nickName: String,
    profileImageUri: String?,
    onImageClick: () -> Unit,
    onNickNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (!profileImageUri.isNullOrEmpty()) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "ProfileImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nickName,
            onValueChange = onNickNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("닉네임") },
            placeholder = { Text("닉네임을 입력해주세요") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
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
            userType = "일반",
            nickName = "달리는약사",
            profileImageUri = null
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
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
            userType = "약사",
            nickName = "친절약사",
            profileImageUri = "https://example.com/image.jpg"
        ),
        onUpdateUserType = {},
        onUpdateEmail = {},
        onCheckEmail = {},
        onUpdatePassword = {},
        onUpdateNickName = {},
        onImageClick = {},
        onNextClick = {}
    )
}