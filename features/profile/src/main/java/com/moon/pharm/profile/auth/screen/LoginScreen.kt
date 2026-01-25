package com.moon.pharm.profile.auth.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.mapper.asString
import com.moon.pharm.profile.auth.model.LoginUiMessage
import com.moon.pharm.profile.auth.screen.component.LoginHeader
import com.moon.pharm.profile.auth.screen.section.LoginFooterSection
import com.moon.pharm.profile.auth.screen.section.LoginInputSection
import com.moon.pharm.profile.auth.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val userMessage = uiState.userMessage
    val messageText = (userMessage as? LoginUiMessage)?.asString()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    LoginScreenContent(
        email = uiState.email,
        password = uiState.password,
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onLoginClick = viewModel::login,
        onSignUpClick = onNavigateToSignUp
    )
}

@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Scaffold(
        containerColor = White,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(50.dp))

            LoginInputSection(
                email = email,
                password = password,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange
            )

            Spacer(modifier = Modifier.height(40.dp))

            PharmPrimaryButton(
                text = stringResource(R.string.login_button_login),
                onClick = onLoginClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            LoginFooterSection(onSignUpClick = onSignUpClick)
        }
    }
}