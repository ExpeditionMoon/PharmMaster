package com.moon.pharm.profile.mypage.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.button.PharmOutlinedButton
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.profile.R

@Composable
fun MyPageFooterSection(
    onLogout: () -> Unit,
    appVersion: String,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SimpleLinkItem(
                    text = stringResource(R.string.mypage_terms_of_use),
                    onClick = onTermsClick
                )
                HorizontalDivider(
                    color = backgroundLight,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                SimpleLinkItem(
                    text = stringResource(R.string.mypage_privacy_policy),
                    onClick = onPrivacyClick
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.mypage_app_version_format, appVersion),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = SecondFont,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        PharmOutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.mypage_logout),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.mypage_withdraw),
                color = SecondFont,
                fontSize = 13.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onWithdrawClick() }
            )
        }
    }
}

@Composable
private fun SimpleLinkItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, fontSize = 14.sp, color = Primary)
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Tertiary
        )
    }
}