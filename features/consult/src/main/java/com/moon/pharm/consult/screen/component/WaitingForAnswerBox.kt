package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R

@Composable
fun WaitingForAnswerBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PharmTheme.colors.surface, RoundedCornerShape(10.dp))
            .border(1.dp, PharmTheme.colors.primary, RoundedCornerShape(10.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = PharmTheme.colors.primary,
            modifier = Modifier.padding(end = 10.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.consult_status_waiting_title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PharmTheme.colors.primary
            )
            Text(
                text = stringResource(R.string.consult_status_waiting_subtitle),
                fontSize = 13.sp,
                color = PharmTheme.colors.secondFont,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
private fun WaitingForAnswerBoxPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WaitingForAnswerBox()
        }
    }
}