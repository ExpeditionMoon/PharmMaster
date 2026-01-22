package com.moon.pharm.profile.auth.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect { success ->
            if (success) onNavigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    LoginScreenContent(
        email = email,
        password = password,
        isLoading = isLoading,
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Scaffold(
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = stringResource(R.string.login_logo_description),
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(stringResource(R.string.login_app_title), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Primary)

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.login_email_label)) },
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.login_password_label)) },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressBar()
                } else {
                    Text(stringResource(R.string.login_button_login), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.login_prompt_no_account), color = SecondFont, fontSize = 12.sp)
                Text(
                    text = stringResource(R.string.login_action_sign_up),
                    color = Secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        email = "test@example.com",
        password = "",
        isLoading = false,
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onSignUpClick = {}
    )
}