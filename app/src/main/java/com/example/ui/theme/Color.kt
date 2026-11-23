package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// SkyPulse Brand Palette — Sky Blue + Premium Pink + Dark Navy
val SkyBlue = Color(0xFF38BDF8)
val SkyBlueDeep = Color(0xFF0284C7)
val LightSkyBlue = Color(0xFF7DD3FC)
val PaleSky = Color(0xFFE0F2FE)

val PremiumPink = Color(0xFFFF75B8)
val PremiumPinkDeep = Color(0xFFDB2777)
val LightPink = Color(0xFFFFC1DC)
val PalePink = Color(0xFFFCE7F3)

val PureWhite = Color(0xFFFFFFFF)
val DarkNavy = Color(0xFF172554)
val NavySlate = Color(0xFF1E3A8A)
val MutedNavy = Color(0xFF475569)
val SoftBackground = Color(0xFFF5FAFF)
val SoftSurfaceTint = Color(0xFFEDF6FF)

// Glassmorphism Surface & Border Tokens
val GlassWhiteHigh = Color(0xE6FFFFFF)
val GlassWhiteMed = Color(0xCCFFFFFF)
val GlassWhiteLow = Color(0x99FFFFFF)
val GlassBorderLight = Color(0xD9FFFFFF)
val GlassSkyTint = Color(0x3838BDF8)
val GlassPinkTint = Color(0x33FF75B8)

// Semantic Status Colors
val EmeraldSuccess = Color(0xFF10B981)
val EmeraldSoft = Color(0xFFD1FAE5)
val AmberWarning = Color(0xFFF59E0B)
val AmberSoft = Color(0xFFFEF3C7)
val RoseError = Color(0xFFEF4444)
val RoseSoft = Color(0xFFFEE2E2)
val VioletAccent = Color(0xFF8B5CF6)

// Reusable Brand Gradients
val SkyToPinkBrush = Brush.linearGradient(
    colors = listOf(SkyBlue, PremiumPink)
)

val DeepSkyToPinkBrush = Brush.linearGradient(
    colors = listOf(SkyBlueDeep, PremiumPinkDeep)
)

val SoftSkyPinkBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFFEBF7FF), SoftBackground, Color(0xFFFFF1F8))
)

val NavyGlowBrush = Brush.linearGradient(
    colors = listOf(DarkNavy, NavySlate, Color(0xFF3B1D5A))
)

val CardSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF38BDF8), Color(0xFF7DD3FC), Color(0xFFC4B5FD))
)

val CardPinkGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFF75B8), Color(0xFFFFA8D3), Color(0xFF7DD3FC))
)

val CardMintSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8), Color(0xFFFF9BD0))
)

val CardCoralPinkGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFEC4899), Color(0xFFFF75B8), Color(0xFF38BDF8))
)

val CardIndigoSkyGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1D4ED8), Color(0xFF38BDF8), Color(0xFFFF75B8))
)
