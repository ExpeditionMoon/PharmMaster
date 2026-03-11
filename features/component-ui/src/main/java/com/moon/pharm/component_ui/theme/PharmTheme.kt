package com.moon.pharm.component_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

fun PharmColorPalette.toMaterialColorScheme(): ColorScheme {
    return lightColorScheme(
        primary = this.primary,
        onPrimary = this.onPrimary,
        primaryContainer = this.primaryContainer,
        onPrimaryContainer = this.onPrimaryContainer,
        secondary = this.secondary,
        onSecondary = this.onSecondary,
        secondaryContainer = this.secondaryContainer,
        onSecondaryContainer = this.onSecondaryContainer,
        tertiary = this.tertiary,
        onTertiary = this.onTertiary,
        tertiaryContainer = this.tertiaryContainer,
        onTertiaryContainer = this.onTertiaryContainer,
        error = this.error,
        onError = this.onError,
        errorContainer = this.errorContainer,
        onErrorContainer = this.onErrorContainer,
        background = this.background,
        onBackground = this.onBackground,
        surface = this.surface,
        onSurface = this.onSurface
    )
}

object PharmTheme {
    val colors: PharmColorPalette
        @Composable
        get() = LocalPharmColors.current

    // 향후 Typography 작업 시 주석 해제하여 사용
    // val typography: PharmTypography
    //     @Composable
    //     get() = LocalPharmTypography.current
}

@Composable
fun PharmMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    CompositionLocalProvider(
        LocalPharmColors provides colors,
        LocalContentColor provides colors.onBackground
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
//            typography = Typography,
            content = content
        )
    }
}