package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun MyConsultEmptyView(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = PharmTheme.colors.secondFont
    )
}

@ThemePreviews
@Composable
private fun MyConsultEmptyViewPreview() {
    PharmMasterTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MyConsultEmptyView(text = "등록된 상담 내역이 없습니다.")
        }
    }
}