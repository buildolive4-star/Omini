package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// SkyPulse Ultra-Premium 3D Glassmorphism Palette — Sky Blue + Electric Pink + Midnight Navy
val SkyBlue = Color(0xFF38BDF8)
val SkyBlueVibrant = Color(0xFF0EA5E9)
val SkyBlueDeep = Color(0xFF0284C7)
val LightSkyBlue = Color(0xFF7DD3FC)
val PaleSky = Color(0xFFE0F2FE)

val PremiumPink = Color(0xFFFF66B2)
val ElectricPink = Color(0xFFFF3D9A)
val PremiumPinkDeep = Color(0xFFDB2777)
val LightPink = Color(0xFFFFC1DC)
val PalePink = Color(0xFFFCE7F3)

val IridescentViolet = Color(0xFF8B5CF6)
val CyberCyan = Color(0xFF22D3EE)

val PureWhite = Color(0xFFFFFFFF)
val DarkNavy = Color(0xFF0F1B3D)
val ObsidianNavy = Color(0xFF070D1F)
val MidnightCard = Color(0xFF121F44)
val NavySlate = Color(0xFF1E3A8A)
val MutedNavy = Color(0xFF475569)
val MutedLightBlue = Color(0xFFBAE6FD)
val SoftBackground = Color(0xFFF3F9FF)
val SoftSurfaceTint = Color(0xFFEBF5FF)

// Glassmorphism Surface & Border Tokens
val GlassWhiteHigh = Color(0xEBFFFFFF)
val GlassWhiteMed = Color(0xD4FFFFFF)
val GlassWhiteLow = Color(0xA6FFFFFF)
val GlassDarkHigh = Color(0xD9121F44)
val GlassDarkMed = Color(0xBF172554)
val GlassBorderLight = Color(0xF2FFFFFF)
val GlassBorderDark = Color(0x667DD3FC)
val GlassSkyTint = Color(0x3D38BDF8)
val GlassPinkTint = Color(0x38FF66B2)

// Semantic Status Colors
val EmeraldSuccess = Color(0xFF10B981)
val EmeraldSoft = Color(0xFFD1FAE5)
val AmberWarning = Color(0xFFF59E0B)
val AmberSoft = Color(0xFFFEF3C7)
val RoseError = Color(0xFFF43F5E)
val RoseSoft = Color(0xFFFFE4E6)
val VioletAccent = Color(0xFF8B5CF6)

// Ultra-Luminous Brand Gradients
val SkyToPinkBrush = Brush.linearGradient(
    colors = listOf(SkyBlueVibrant, SkyBlue, PremiumPink, ElectricPink)
)

val DeepSkyToPinkBrush = Brush.linearGradient(
    colors = listOf(SkyBlueDeep, IridescentViolet, PremiumPinkDeep)
)

val SoftSkyPinkBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFFE3F4FF), Color(0xFFF5FAFF), Color(0xFFFFEEF7), Color(0xFFEBF5FF))
)

val DarkAuroraBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF060C1F), Color(0xFF0D1838), Color(0xFF1B123A), Color(0xFF081229))
)

val NavyGlowBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF0B1437), Color(0xFF172B69), Color(0xFF5A166A))
)

val CardSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF818CF8))
)

val CardPinkGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFEC4899), Color(0xFFFF66B2), Color(0xFF38BDF8))
)

val CardMintSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF22D3EE), Color(0xFFFF75B8))
)

val CardCoralPinkGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFDB2777), Color(0xFFFF5CA8), Color(0xFF60A5FA))
)

val CardIndigoSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1E3A8A), Color(0xFF0EA5E9), Color(0xFFFF66B2))
)

val SpecularSheenBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.55f),
        Color.White.copy(alpha = 0.14f),
        Color.Transparent
    )
)
