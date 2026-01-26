package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryContainerLight
import com.moon.pharm.consult.R

@Composable
fun ConsultConfirmContent(
    title: String,
    content: String,
    selectedPharmacistName: String,
    onEditTitleOrContent: () -> Unit,
    onEditPharmacist: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.consult_confirm_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryLight,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(secondaryContainerLight, RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoCard(
                label = stringResource(R.string.consult_confirm_label_content),
                content = title.ifBlank { stringResource(R.string.consult_write_empty_value) },
                onEditClick = onEditTitleOrContent
            )
            InfoCard(
                label = stringResource(R.string.consult_confirm_label_content),
                content = content.ifBlank { stringResource(R.string.consult_write_empty_value) },
                onEditClick = onEditTitleOrContent
            )
            InfoCard(
                label = stringResource(R.string.consult_confirm_label_pharmacist),
                content = selectedPharmacistName,
                onEditClick = onEditPharmacist
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        GuidanceBox(
            text = stringResource(R.string.consult_confirm_guidance)
        )

        Spacer(modifier = Modifier.weight(1f))

        PharmPrimaryButton(
            text = stringResource(R.string.consult_confirm_submit_btn),
            onClick = onSubmit
        )
    }
}