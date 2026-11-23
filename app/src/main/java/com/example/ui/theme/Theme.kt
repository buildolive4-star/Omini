package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LocalIsDarkGlass = compositionLocalOf { false }

private val ColorBorderSubtle = Color(0xFFD6EAFF)
private val ColorBorderDarkSubtle = Color(0xFF263B73)

private val SkyPulseLightColorScheme = lightColorScheme(
    primary = SkyBlueVibrant,
    onPrimary = PureWhite,
    primaryContainer = PaleSky,
    onPrimaryContainer = DarkNavy,
    secondary = PremiumPink,
    onSecondary = PureWhite,
    secondaryContainer = PalePink,
    onSecondaryContainer = DarkNavy,
    tertiary = IridescentViolet,
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

private val SkyPulseDarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    onPrimary = ObsidianNavy,
    primaryContainer = Color(0xFF15325E),
    onPrimaryContainer = PureWhite,
    secondary = PremiumPink,
    onSecondary = ObsidianNavy,
    secondaryContainer = Color(0xFF4B1648),
    onSecondaryContainer = PureWhite,
    tertiary = CyberCyan,
    onTertiary = ObsidianNavy,
    background = ObsidianNavy,
    onBackground = PureWhite,
    surface = MidnightCard,
    onSurface = PureWhite,
    surfaceVariant = Color(0xFF1A2954),
    onSurfaceVariant = MutedLightBlue,
    outline = SkyBlue,
    outlineVariant = ColorBorderDarkSubtle,
    error = RoseError,
    onError = PureWhite
)

val SkyPulseShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(34.dp)
)

@Composable
fun SkyPulseTheme(
    darkGlassTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalIsDarkGlass provides darkGlassTheme) {
        MaterialTheme(
            colorScheme = if (darkGlassTheme) SkyPulseDarkColorScheme else SkyPulseLightColorScheme,
            typography = SkyPulseTypography,
            shapes = SkyPulseShapes,
            content = content
        )
    }
}
