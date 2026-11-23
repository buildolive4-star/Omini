package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ColorBorderSubtle = androidx.compose.ui.graphics.Color(0xFFD6EAFF)

private val SkyPulseLightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = DarkNavy,
    primaryContainer = PaleSky,
    onPrimaryContainer = DarkNavy,
    secondary = PremiumPink,
    onSecondary = PureWhite,
    secondaryContainer = PalePink,
    onSecondaryContainer = DarkNavy,
    tertiary = NavySlate,
    onTertiary = PureWhite,
    tertiaryContainer = LightSkyBlue,
    onTertiaryContainer = DarkNavy,
    background = SoftBackground,
    onBackground = DarkNavy,
    surface = PureWhite,
    onSurface = DarkNavy,
    surfaceVariant = SoftSurfaceTint,
    onSurfaceVariant = MutedNavy,
    outline = LightSkyBlue,
    outlineVariant = ColorBorderSubtle,
    error = RoseError,
    onError = PureWhite,
    errorContainer = RoseSoft,
    onErrorContainer = DarkNavy
)

val SkyPulseShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun SkyPulseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SkyPulseLightColorScheme,
        typography = SkyPulseTypography,
        shapes = SkyPulseShapes,
        content = content
    )
}
