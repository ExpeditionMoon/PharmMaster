package com.moon.pharm.component_ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * 라이트 모드와 다크 모드를 동시에 확인하기 위한 멀티 프리뷰 어노테이션
 */
@Preview(
    name = "1. Light Mode",
    group = "Themes",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Preview(
    name = "2. Dark Mode",
    group = "Themes",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF121212
)
annotation class ThemePreviews