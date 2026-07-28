package com.moon.pharm.component_ui.component.fab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun PharmPrescriptionFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        containerColor = PharmTheme.colors.primary
    ) {
        Image(
            painter = painterResource(id = R.drawable.prescription_image),
            contentDescription = "처방전",
            modifier = Modifier.size(30.dp),
        )
    }
}

@ThemePreviews
@Composable
private fun PharmPrescriptionFABPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PharmPrescriptionFAB(onClick = {})
        }
    }
}