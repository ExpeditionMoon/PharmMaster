package com.moon.pharm.consult.screen.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moon.pharm.component_ui.theme.SecondFont

@Composable
fun MyConsultEmptyView(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = SecondFont
    )
}