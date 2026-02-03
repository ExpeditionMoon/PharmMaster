package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Success
import com.moon.pharm.component_ui.theme.SuccessContainer
import com.moon.pharm.component_ui.theme.Warning
import com.moon.pharm.component_ui.theme.WarningContainer
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel

@Composable
fun HistoryRecordItem(
    uiModel: HistoryRecordUiModel,
    onRecordClick: () -> Unit
) {
    val record = uiModel.record
    val borderColor = if (record.isTaken) Success else Warning
    val backgroundColor = if (record.isTaken) SuccessContainer else WarningContainer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecordClick() }
            .background(backgroundColor, RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = uiModel.medicationName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text =
                    if (record.isTaken) stringResource(R.string.medication_take_on)
                    else stringResource(R.string.medication_take_off),
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = if (record.isTaken) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(24.dp)
        )
    }
}