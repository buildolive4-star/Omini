package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkAuroraBackgroundBrush
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassBorderDark
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassDarkHigh
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.IridescentViolet
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.LocalIsDarkGlass
import com.example.ui.theme.MutedLightBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyBlueVibrant
import com.example.ui.theme.SkyToPinkBrush
import com.example.ui.theme.SoftSkyPinkBackgroundBrush
import kotlin.math.PI
import kotlin.math.sin

/**
 * Ultra-Premium 3D Aurora Mesh Glass Background (supports Pearl Light Glass & Cyber Midnight Dark Glass).
 */
@Composable
fun AnimatedSkyPinkBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = LocalIsDarkGlass.current
    val infinite = rememberInfiniteTransition(label = "aurora_mesh")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_drift"
    )
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(11000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) DarkAuroraBackgroundBrush else SoftSkyPinkBackgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val orbAlpha = if (isDark) 0.34f else 0.28f

            // Top-left Sky Blue luminous aurora orb
            val c1 = Offset(w * (0.12f + 0.12f * drift), h * (0.08f + 0.05f * drift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SkyBlue.copy(alpha = orbAlpha), Color.Transparent),
                    center = c1,
                    radius = w * 0.68f
                ),
                radius = w * 0.68f,
                center = c1
            )

            // Right Electric Pink aurora orb
            val c2 = Offset(w * (0.90f - 0.10f * drift), h * (0.26f + 0.06f * drift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(ElectricPink.copy(alpha = orbAlpha * 0.92f), Color.Transparent),
                    center = c2,
                    radius = w * 0.64f
                ),
                radius = w * 0.64f,
                center = c2
            )

            // Mid-left Iridescent Violet accent orb
            val c3 = Offset(w * (0.20f + 0.08f * drift), h * (0.58f - 0.04f * drift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(IridescentViolet.copy(alpha = orbAlpha * 0.65f), Color.Transparent),
                    center = c3,
                    radius = w * 0.54f
                ),
                radius = w * 0.54f,
                center = c3
            )

            // Bottom-right Cyber Cyan glow
            val c4 = Offset(w * (0.78f - 0.06f * drift), h * (0.86f - 0.04f * drift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyberCyan.copy(alpha = orbAlpha * 0.7f), Color.Transparent),
                    center = c4,
                    radius = w * 0.58f
                ),
                radius = w * 0.58f,
                center = c4
            )

            // Translucent flowing ribbon wave
            val wavePath = Path().apply {
                moveTo(0f, h * 0.24f)
                val step = w / 24f
                for (i in 0..24) {
                    val x = i * step
                    val y = h * 0.24f + sin(phase + i * 0.35f) * 26f
                    lineTo(x, y)
                }
            }
            drawPath(
                path = wavePath,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        SkyBlue.copy(alpha = if (isDark) 0.24f else 0.18f),
                        PremiumPink.copy(alpha = if (isDark) 0.24f else 0.18f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )

            // Floating sparkle particles
            val dotColor = if (isDark) LightSkyBlue.copy(alpha = 0.45f) else PureWhite.copy(alpha = 0.72f)
            listOf(
                Offset(w * 0.18f, h * (0.16f + 0.02f * drift)),
                Offset(w * 0.82f, h * (0.14f - 0.02f * drift)),
                Offset(w * 0.64f, h * (0.42f + 0.03f * drift)),
                Offset(w * 0.26f, h * (0.72f - 0.02f * drift)),
                Offset(w * 0.86f, h * (0.68f + 0.02f * drift))
            ).forEachIndexed { i, pos ->
                drawCircle(dotColor, radius = if (i % 2 == 0) 4.5f else 3f, center = pos)
            }
        }
        content()
    }
}

/**
 * True 3D Extruded Glassmorphism Card with specular reflection highlight, iridescent rim border,
 * soft glowing under-bezel, and bouncy 3D perspective press tilt.
 */
@Composable
fun Glass3DCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 26.dp,
    gradientBrush: Brush? = null,
    containerColor: Color? = null,
    elevation: Dp = 14.dp,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = LocalIsDarkGlass.current
    val resolvedContainer = containerColor ?: if (isDark) GlassDarkHigh else GlassWhiteHigh

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 0.965f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )
    val tiltX by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 5f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_tilt_x"
    )
    val liftY by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 4f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_lift_y"
    )

    val shape = RoundedCornerShape(cornerRadius)
    val bgModifier = if (gradientBrush != null) {
        Modifier.background(gradientBrush, shape)
    } else {
        Modifier.background(
            Brush.verticalGradient(
                listOf(
                    resolvedContainer,
                    resolvedContainer.copy(alpha = if (isDark) 0.80f else 0.92f)
                )
            ),
            shape
        )
    }

    val borderBrush = if (gradientBrush != null) {
        Brush.linearGradient(
            listOf(
                PureWhite.copy(alpha = 0.85f),
                LightSkyBlue.copy(alpha = 0.55f),
                LightPink.copy(alpha = 0.65f),
                PureWhite.copy(alpha = 0.4f)
            )
        )
    } else if (isDark) {
        Brush.linearGradient(
            listOf(
                GlassBorderDark,
                PremiumPink.copy(alpha = 0.45f),
                SkyBlue.copy(alpha = 0.35f)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                GlassBorderLight,
                SkyBlue.copy(alpha = 0.45f),
                PremiumPink.copy(alpha = 0.42f),
                PureWhite
            )
        )
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationX = tiltX
                translationY = liftY
                cameraDistance = 14f * density
            }
            // Subtle 3D bottom-lip glow depth
            .drawBehind {
                val radiusPx = cornerRadius.toPx()
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(
                            SkyBlue.copy(alpha = if (isDark) 0.32f else 0.24f),
                            PremiumPink.copy(alpha = if (isDark) 0.34f else 0.26f)
                        )
                    ),
                    topLeft = Offset(x = 8f, y = 10f),
                    size = Size(width = (size.width - 16f).coerceAtLeast(0f), height = size.height),
                    cornerRadius = CornerRadius(radiusPx, radiusPx)
                )
            }
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = SkyBlue.copy(alpha = 0.32f),
                spotColor = PremiumPink.copy(alpha = 0.34f)
            )
            .clip(shape)
            .then(bgModifier)
            // Specular diagonal glass light sheen
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PureWhite.copy(alpha = if (gradientBrush != null) 0.26f else if (isDark) 0.11f else 0.45f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.width * 0.65f, size.height * 0.55f)
                    )
                )
            }
            .border(width = 1.4.dp, brush = borderBrush, shape = shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = androidx.compose.material3.ripple(),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * Animated Shimmering Sky-to-Pink 3D Gradient CTA Pill Button with glossy sheen sweep.
 */
@Composable
fun GradientPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    brush: Brush = SkyToPinkBrush,
    testTagName: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btn_scale"
    )
    val infinite = rememberInfiniteTransition(label = "btn_sheen")
    val sheenProgress by infinite.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sheen_pos"
    )
    val shape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .then(if (testTagName != null) Modifier.testTag(testTagName) else Modifier)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .heightIn(min = 48.dp)
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = shape,
                spotColor = ElectricPink.copy(alpha = 0.52f),
                ambientColor = SkyBlueVibrant.copy(alpha = 0.48f)
            )
            .clip(shape)
            .background(
                if (enabled) brush else Brush.linearGradient(listOf(Color(0xFF94A3B8), Color(0xFF64748B)))
            )
            .drawWithContent {
                drawContent()
                if (enabled) {
                    val x = size.width * sheenProgress
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Transparent, PureWhite.copy(alpha = 0.32f), Color.Transparent),
                            start = Offset(x - 90f, 0f),
                            end = Offset(x + 90f, size.height)
                        )
                    )
                }
            }
            .border(1.2.dp, PureWhite.copy(alpha = 0.65f), shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(color = PureWhite),
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PureWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = PureWhite,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/**
 * Reusable Top Header for sub-screens adapting cleanly to Pearl Light & Cyber Dark Glass.
 */
@Composable
fun GlassSubScreenHeader(
    title: String,
    subtitle: String? = null,
    onBack: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    val isDark = LocalIsDarkGlass.current
    val titleColor = if (isDark) PureWhite else DarkNavy
    val subtitleColor = if (isDark) MutedLightBlue else MutedNavy

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = if (isDark) GlassDarkHigh else GlassWhiteHigh,
            border = BorderStroke(1.2.dp, SkyBlue.copy(alpha = 0.55f)),
            shadowElevation = 6.dp
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("subscreen_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = titleColor
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor,
                fontWeight = FontWeight.ExtraBold
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = subtitleColor
                )
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

/**
 * Animated Multi-Step Live Progress Stepper with glowing nodes.
 */
@Composable
fun AnimatedOrderStepper(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkGlass.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, label ->
            val reached = index <= currentStep
            val isCurrent = index == currentStep
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.5.dp)
                            .background(
                                if (index == 0) Color.Transparent
                                else if (reached) SkyBlue else PaleSky.copy(alpha = if (isDark) 0.25f else 1f)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 26.dp else 20.dp)
                            .shadow(if (isCurrent) 8.dp else 0.dp, CircleShape, spotColor = PremiumPink)
                            .clip(CircleShape)
                            .background(
                                if (reached) SkyToPinkBrush
                                else Brush.linearGradient(listOf(PaleSky, PalePink))
                            )
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = PureWhite,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (reached) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.5.dp)
                            .background(
                                if (index == steps.lastIndex) Color.Transparent
                                else if (index < currentStep) PremiumPink else PaleSky.copy(alpha = if (isDark) 0.25f else 1f)
                            )
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        reached && isDark -> PureWhite
                        reached -> DarkNavy
                        isDark -> MutedLightBlue.copy(alpha = 0.65f)
                        else -> MutedNavy
                    },
                    fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 2
                )
            }
        }
    }
}

/**
 * Futuristic Neon Glass Route Map Canvas with glowing bezier path and radar pulse.
 */
@Composable
fun InteractiveRouteMapCanvas(
    pickupLabel: String,
    dropLabel: String,
    progress: Float,
    vehicleTag: String,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkGlass.current
    val infinite = rememberInfiniteTransition(label = "map_pulse")
    val pulse by infinite.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_ring"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .shadow(14.dp, RoundedCornerShape(26.dp), ambientColor = SkyBlue, spotColor = PremiumPink)
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (isDark) {
                    Brush.linearGradient(listOf(Color(0xFF0A1432), Color(0xFF14224A), Color(0xFF281548)))
                } else {
                    Brush.linearGradient(listOf(Color(0xFFD9F2FF), Color(0xFFF3FAFF), Color(0xFFFFE6F3)))
                }
            )
            .border(1.5.dp, if (isDark) SkyBlue.copy(alpha = 0.5f) else PureWhite, RoundedCornerShape(26.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val roadColor = if (isDark) SkyBlue.copy(alpha = 0.16f) else PureWhite.copy(alpha = 0.9f)

            for (i in 1..4) {
                val y = h * (i / 5f)
                drawLine(roadColor, Offset(0f, y), Offset(w, y), strokeWidth = 5f)
            }
            for (i in 1..5) {
                val x = w * (i / 6f)
                drawLine(roadColor, Offset(x, 0f), Offset(x, h), strokeWidth = 4f)
            }

            drawCircle(SkyBlue.copy(alpha = if (isDark) 0.18f else 0.35f), radius = 42f, center = Offset(w * 0.28f, h * 0.30f))
            drawCircle(PremiumPink.copy(alpha = if (isDark) 0.18f else 0.35f), radius = 48f, center = Offset(w * 0.74f, h * 0.68f))

            val start = Offset(w * 0.15f, h * 0.72f)
            val ctrl = Offset(w * 0.48f, h * 0.16f)
            val end = Offset(w * 0.85f, h * 0.34f)

            val routePath = Path().apply {
                moveTo(start.x, start.y)
                quadraticTo(ctrl.x, ctrl.y, end.x, end.y)
            }

            drawPath(
                path = routePath,
                color = SkyBlue.copy(alpha = 0.30f),
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            drawPath(
                path = routePath,
                brush = Brush.linearGradient(listOf(CyberCyan, SkyBlue, ElectricPink)),
                style = Stroke(
                    width = 7.5f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
                )
            )

            // Pickup Pin
            drawCircle(SkyBlue.copy(alpha = 0.28f), radius = 24f, center = start)
            drawCircle(SkyBlueVibrant, radius = 12f, center = start)
            drawCircle(PureWhite, radius = 5f, center = start)

            // Drop Pin
            drawCircle(ElectricPink.copy(alpha = 0.28f), radius = 24f, center = end)
            drawCircle(ElectricPink, radius = 12f, center = end)
            drawCircle(PureWhite, radius = 5f, center = end)

            val t = progress.coerceIn(0f, 1f)
            val inv = 1f - t
            val mx = inv * inv * start.x + 2 * inv * t * ctrl.x + t * t * end.x
            val my = inv * inv * start.y + 2 * inv * t * ctrl.y + t * t * end.y
            val marker = Offset(mx, my)

            drawCircle(
                color = ElectricPink.copy(alpha = (1.75f - pulse).coerceIn(0f, 0.50f)),
                radius = 20f * pulse,
                center = marker
            )
            drawCircle(DarkNavy, radius = 15f, center = marker)
            drawCircle(CyberCyan, radius = 8f, center = marker)
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = DarkNavy.copy(alpha = 0.88f),
            border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.6f)),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        ) {
            Text(
                text = "3D LIVE HUD • $vehicleTag",
                style = MaterialTheme.typography.labelSmall,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) GlassDarkHigh else GlassWhiteHigh,
                border = BorderStroke(1.dp, SkyBlue)
            ) {
                Text(
                    text = "A: ${pickupLabel.take(18)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) PureWhite else DarkNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) GlassDarkHigh else GlassWhiteHigh,
                border = BorderStroke(1.dp, PremiumPink)
            ) {
                Text(
                    text = "B: ${dropLabel.take(18)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) PureWhite else DarkNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun GlassChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkGlass.current
    val shape = RoundedCornerShape(50)
    Surface(
        modifier = modifier
            .heightIn(min = 38.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = when {
            selected -> Color.Transparent
            isDark -> GlassDarkHigh
            else -> GlassWhiteHigh
        },
        border = BorderStroke(
            width = 1.2.dp,
            color = if (selected) PureWhite.copy(alpha = 0.7f) else SkyBlue.copy(alpha = 0.45f)
        ),
        shadowElevation = if (selected) 8.dp else 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .then(if (selected) Modifier.background(SkyToPinkBrush, shape) else Modifier)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected || isDark) PureWhite else DarkNavy,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StatusPill(
    text: String,
    color: Color = EmeraldSuccess
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.18f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.55f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ShimmerSkeletonCard(
    modifier: Modifier = Modifier,
    height: Dp = 84.dp
) {
    val infinite = rememberInfiniteTransition(label = "shimmer")
    val alpha by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        PaleSky.copy(alpha = alpha),
                        PureWhite.copy(alpha = alpha),
                        PalePink.copy(alpha = alpha)
                    )
                )
            )
            .border(1.2.dp, PureWhite.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
    )
}
