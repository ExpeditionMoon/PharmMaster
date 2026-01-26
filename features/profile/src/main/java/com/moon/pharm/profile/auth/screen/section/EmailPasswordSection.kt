package com.moon.pharm.profile.auth.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.clickableSingle
import com.moon.pharm.profile.R

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
            placeholder = { Text(stringResource(R.string.signup_email_placeholder)) },
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = if (isEmailChecking || isAvailable == true) Color.Gray else Primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickableSingle(enabled = !isEmailChecking && isAvailable != true) {
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
                            text =
                                if (isAvailable == true) stringResource(R.string.signup_email_check_complete)
                                else stringResource(R.string.signup_email_check_button),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }
            }
        )
        if (isAvailable == true) {
            Text(
                stringResource(R.string.signup_email_available),
                color = Primary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )
        } else if (isAvailable == false) {
            Text(
                stringResource(R.string.signup_email_duplicated),
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )
        }

        if (isAvailable == true) {
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onUpdatePassword,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.signup_password_label)) },
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
