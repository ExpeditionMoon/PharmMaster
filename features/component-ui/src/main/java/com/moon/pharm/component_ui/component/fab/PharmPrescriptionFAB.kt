package com.moon.pharm.component_ui.component.fab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.Primary

@Composable
fun PharmPrescriptionFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        containerColor = Primary
    ) {
        Image(
            painter = painterResource(id = R.drawable.prescription_image),
            contentDescription = "처방전",
            modifier = Modifier.size(30.dp),
        )
    }
}