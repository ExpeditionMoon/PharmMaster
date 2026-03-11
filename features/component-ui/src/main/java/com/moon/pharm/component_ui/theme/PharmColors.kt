package com.moon.pharm.component_ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Primitive Colors
private val BluePrimary = Color(0xFF1F3B58)
private val BrownSecondary = Color(0xFF5E4B3B)
private val BlueTertiary = Color(0xFFB1CBDC)
private val WhiteColor = Color(0xFFFFFFFF)
private val BlackColor = Color(0xFF000000)
private val BackgroundColor = Color(0xFFF2F4F7)
private val GraySecondFont = Color(0xFF767676)
private val GrayPlaceholder = Color(0xFFBDBDBD)
// --- 스낵바 및 상태 표시용 원시 색상 ---
private val GreenSuccess = Color(0xFF2E7D32)
private val GreenSuccessContainer = Color(0xFFE8F5E9)
private val BlueInfoContainer = Color(0xFFE3F2FD)
private val OrangeWarning = Color(0xFFF57C00)
private val OrangeWarningContainer = Color(0xFFFFF3E0)

// Semantic Colors
data class PharmColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val placeholder: Color,
    val secondFont: Color,

    // 커스텀 상태 색상
    val success: Color,
    val successContainer: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,
    val warning: Color,
    val warningContainer: Color
)

// 라이트 모드
val LightColorPalette = PharmColorPalette(
    primary = BluePrimary,
    onPrimary = WhiteColor,
    primaryContainer = BlueTertiary,
    onPrimaryContainer = Color(0xFF001E30),

    secondary = BrownSecondary,
    onSecondary = WhiteColor,
    secondaryContainer = Color(0xFFE6DED5),
    onSecondaryContainer = Color(0xFF24190F),

    tertiary = BlueTertiary,
    onTertiary = Color(0xFF00344D),
    tertiaryContainer = Color(0xFFCDE6F6),
    onTertiaryContainer = Color(0xFF001E30),

    error = Color(0xFFBA1A1A),
    onError = WhiteColor,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = BackgroundColor,
    onBackground = BlackColor,
    surface = WhiteColor,
    onSurface = BlackColor,
    placeholder = GrayPlaceholder,
    secondFont = GraySecondFont,

    success = GreenSuccess,
    successContainer = GreenSuccessContainer,
    infoContainer = BlueInfoContainer,
    onInfoContainer = BluePrimary,
    warning = OrangeWarning,
    warningContainer = OrangeWarningContainer
)

// 다크 모드
val DarkColorPalette = LightColorPalette.copy(
    // TODO: 향후 다크 모드용 색상으로 덮어쓰기
)

// CompositionLocal
val LocalPharmColors = staticCompositionLocalOf<PharmColorPalette> {
    error("PharmColors not provided. Did you wrap your content in PharmMasterTheme?")
}