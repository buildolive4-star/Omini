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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyToPinkBrush
import com.example.ui.theme.SoftSkyPinkBackgroundBrush

/**
 * Full-bleed atmospheric Sky Blue & Pink animated glass backdrop.
 */
@Composable
fun AnimatedSkyPinkBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "bg_orbs")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_drift"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoftSkyPinkBackgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SkyBlue.copy(alpha = 0.24f), Color.Transparent),
                    center = Offset(w * (0.15f + 0.08f * drift), h * (0.09f + 0.04f * drift)),
                    radius = w * 0.58f
                ),
                radius = w * 0.58f,
                center = Offset(w * (0.15f + 0.08f * drift), h * (0.09f + 0.04f * drift))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PremiumPink.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(w * (0.88f - 0.07f * drift), h * (0.28f + 0.05f * drift)),
                    radius = w * 0.55f
                ),
                radius = w * 0.55f,
                center = Offset(w * (0.88f - 0.07f * drift), h * (0.28f + 0.05f * drift))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LightSkyBlue.copy(alpha = 0.20f), Color.Transparent),
                    center = Offset(w * (0.22f + 0.05f * drift), h * (0.78f - 0.04f * drift)),
                    radius = w * 0.5f
                ),
                radius = w * 0.5f,
                center = Offset(w * (0.22f + 0.05f * drift), h * (0.78f - 0.04f * drift))
            )
        }
        content()
    }
}

/**
 * 3D Floating Glassmorphism Card with specular highlight border and spring tilt interaction.
 */
@Composable
fun Glass3DCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    gradientBrush: Brush? = null,
    containerColor: Color = GlassWhiteHigh,
    elevation: Dp = 10.dp,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )
    val tiltX by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 3.5f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_tilt"
    )

    val shape = RoundedCornerShape(cornerRadius)
    val bgModifier = if (gradientBrush != null) {
        Modifier.background(gradientBrush, shape)
    } else {
        Modifier.background(containerColor, shape)
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationX = tiltX
                cameraDistance = 12f * density
            }
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = SkyBlue.copy(alpha = 0.22f),
                spotColor = PremiumPink.copy(alpha = 0.24f)
            )
            .clip(shape)
            .then(bgModifier)
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(GlassBorderLight, SkyBlue.copy(alpha = 0.35f), PremiumPink.copy(alpha = 0.35f))
                ),
                shape = shape
            )
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
 * Animated Sky-Blue-to-Pink 3D gradient CTA button (enforces >= 48.dp touch target).
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
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btn_scale"
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
                elevation = if (enabled) 8.dp else 0.dp,
                shape = shape,
                spotColor = PremiumPink.copy(alpha = 0.4f),
                ambientColor = SkyBlue.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(
                if (enabled) brush else Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8)))
            )
            .border(1.dp, PureWhite.copy(alpha = 0.55f), shape)
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
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Reusable Top Header for sub-screens with glass back button & optional right badge/action.
 */
@Composable
fun GlassSubScreenHeader(
    title: String,
    subtitle: String? = null,
    onBack: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = GlassWhiteHigh,
            border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.4f)),
            shadowElevation = 4.dp
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
                    tint = DarkNavy
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = DarkNavy,
                fontWeight = FontWeight.Bold
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedNavy
                )
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

/**
 * Animated Multi-Step Live Progress Stepper for Rides, Food, Grocery, Parcel & Transport.
 */
@Composable
fun AnimatedOrderStepper(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
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
                            .height(3.dp)
                            .background(
                                if (index == 0) Color.Transparent
                                else if (reached) SkyBlue else PaleSky
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 24.dp else 20.dp)
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
                            .height(3.dp)
                            .background(
                                if (index == steps.lastIndex) Color.Transparent
                                else if (index < currentStep) PremiumPink else PaleSky
                            )
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (reached) DarkNavy else MutedNavy,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 2
                )
            }
        }
    }
}

/**
 * Animated Interactive Hyperlocal Map Canvas with simulated glowing route & moving marker.
 */
@Composable
fun InteractiveRouteMapCanvas(
    pickupLabel: String,
    dropLabel: String,
    progress: Float,
    vehicleTag: String,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "map_pulse")
    val pulse by infinite.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_ring"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFDFF4FF), Color(0xFFF0F9FF), Color(0xFFFFEAF5))
                )
            )
            .border(1.5.dp, PureWhite, RoundedCornerShape(24.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Stylized city road grid
            val roadColor = PureWhite.copy(alpha = 0.85f)
            for (i in 1..4) {
                val y = h * (i / 5f)
                drawLine(roadColor, Offset(0f, y), Offset(w, y), strokeWidth = 5f)
            }
            for (i in 1..5) {
                val x = w * (i / 6f)
                drawLine(roadColor, Offset(x, 0f), Offset(x, h), strokeWidth = 4f)
            }

            // Soft city park blocks
            drawCircle(Color(0xFFBAE6FD).copy(alpha = 0.45f), radius = 38f, center = Offset(w * 0.28f, h * 0.30f))
            drawCircle(Color(0xFFFBCFE8).copy(alpha = 0.45f), radius = 44f, center = Offset(w * 0.74f, h * 0.68f))

            val start = Offset(w * 0.16f, h * 0.72f)
            val ctrl = Offset(w * 0.48f, h * 0.18f)
            val end = Offset(w * 0.84f, h * 0.34f)

            // Route curve
            val routePath = Path().apply {
                moveTo(start.x, start.y)
                quadraticTo(ctrl.x, ctrl.y, end.x, end.y)
            }

            // Route glow
            drawPath(
                path = routePath,
                color = SkyBlue.copy(alpha = 0.25f),
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
            // Dashed neon route
            drawPath(
                path = routePath,
                brush = Brush.linearGradient(listOf(SkyBlue, PremiumPink)),
                style = Stroke(
                    width = 7f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
                )
            )

            // Pickup Pin
            drawCircle(SkyBlue.copy(alpha = 0.25f), radius = 22f, center = start)
            drawCircle(SkyBlue, radius = 11f, center = start)
            drawCircle(PureWhite, radius = 5f, center = start)

            // Drop Pin
            drawCircle(PremiumPink.copy(alpha = 0.25f), radius = 22f, center = end)
            drawCircle(PremiumPink, radius = 11f, center = end)
            drawCircle(PureWhite, radius = 5f, center = end)

            // Moving Vehicle Marker along quadratic Bezier
            val t = progress.coerceIn(0f, 1f)
            val inv = 1f - t
            val mx = inv * inv * start.x + 2 * inv * t * ctrl.x + t * t * end.x
            val my = inv * inv * start.y + 2 * inv * t * ctrl.y + t * t * end.y
            val marker = Offset(mx, my)

            drawCircle(
                color = PremiumPink.copy(alpha = (1.6f - pulse).coerceIn(0f, 0.45f)),
                radius = 18f * pulse,
                center = marker
            )
            drawCircle(DarkNavy, radius = 14f, center = marker)
            drawCircle(SkyBlue, radius = 8f, center = marker)
        }

        // Top-left simulated badge
        Surface(
            shape = RoundedCornerShape(50),
            color = DarkNavy.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        ) {
            Text(
                text = "LIVE SIMULATED MAP • $vehicleTag",
                style = MaterialTheme.typography.labelSmall,
                color = PureWhite,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        // Pickup & drop chips
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GlassWhiteHigh,
                border = BorderStroke(1.dp, SkyBlue)
            ) {
                Text(
                    text = "A: ${pickupLabel.take(18)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GlassWhiteHigh,
                border = BorderStroke(1.dp, PremiumPink)
            ) {
                Text(
                    text = "B: ${dropLabel.take(18)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
    val shape = RoundedCornerShape(50)
    Surface(
        modifier = modifier
            .heightIn(min = 38.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = if (selected) DarkNavy else GlassWhiteHigh,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) SkyBlue else LightSkyBlue.copy(alpha = 0.6f)
        ),
        shadowElevation = if (selected) 5.dp else 1.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) PureWhite else DarkNavy,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
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
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
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
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        PaleSky.copy(alpha = alpha),
                        PureWhite.copy(alpha = alpha),
                        PalePink.copy(alpha = alpha)
                    )
                )
            )
            .border(1.dp, PureWhite.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
    )
}
