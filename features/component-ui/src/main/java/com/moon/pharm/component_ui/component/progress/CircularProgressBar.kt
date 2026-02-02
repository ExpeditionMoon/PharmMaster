package com.moon.pharm.component_ui.component.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moon.pharm.component_ui.theme.tertiaryLight

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = tertiaryLight
        )
    }
}