package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.dialog.PharmInfoDialog
import com.moon.pharm.component_ui.component.toggle.CustomSwitch
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.profile.R

@Composable
fun MedicationSwitchRow(
    title: String,
    description: String? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showInfoIcon: Boolean = true,
    explanation: String? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = title, fontSize = 14.sp, color = Black, fontWeight = FontWeight.Medium)
                if (description != null) {
                    Text(text = description, fontSize = 12.sp, color = SecondFont)
                }
            }

            if (showInfoIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.medication_info),
                    tint = SecondFont,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable(enabled = explanation != null) {
                            if (explanation != null) showDialog = true
                        }
                        .padding(2.dp)
                )
            }
        }

        CustomSwitch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }

    if (showDialog && explanation != null) {
        PharmInfoDialog(
            title = title,
            content = explanation,
            onDismiss = { showDialog = false }
        )
    }
}