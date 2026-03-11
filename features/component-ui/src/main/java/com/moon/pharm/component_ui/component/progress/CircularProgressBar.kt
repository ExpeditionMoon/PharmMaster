package com.moon.pharm.component_ui.component.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = PharmTheme.colors.tertiary
        )
    }
}

@ThemePreviews
@Composable
private fun CircularProgressBarPreview() {
    PharmMasterTheme {
        Box(
            modifier = Modifier.size(100.dp).background(PharmTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar()
        }
    }
}