package com.example.ui.screens

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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.NavyGlowBrush
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyToPinkBrush
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var revealed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        revealed = true
        delay(2100L)
        onFinished()
    }

    val scale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.55f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "splash_logo_scale"
    )
    val rotationYAnim by animateFloatAsState(
        targetValue = if (revealed) 0f else -28f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "splash_3d_tilt"
    )

    val infinite = rememberInfiniteTransition(label = "splash_orbit")
    val orbitAngle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_angle"
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyGlowBrush)
            .clickable { onFinished() }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Orbiting 3D rings
        Canvas(
            modifier = Modifier
                .size(270.dp)
                .graphicsLayer { rotationZ = orbitAngle }
        ) {
            drawCircle(
                brush = Brush.sweepGradient(listOf(SkyBlue, PremiumPink, Color.Transparent, SkyBlue)),
                radius = size.minDimension * 0.48f,
                style = Stroke(width = 6f)
            )
            drawCircle(
                brush = Brush.sweepGradient(listOf(PremiumPink, Color.Transparent, LightSkyBlue, PremiumPink)),
                radius = size.minDimension * 0.37f,
                style = Stroke(width = 4f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(136.dp)
                    .graphicsLayer {
                        scaleX = scale * pulse
                        scaleY = scale * pulse
                        rotationY = rotationYAnim
                        cameraDistance = 12f * density
                    }
                    .shadow(24.dp, RoundedCornerShape(34.dp), spotColor = PremiumPink, ambientColor = SkyBlue)
                    .clip(RoundedCornerShape(34.dp))
                    .background(DarkNavy)
                    .border(2.5.dp, SkyToPinkBrush, RoundedCornerShape(34.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_fg),
                    contentDescription = "SkyPulse 3D Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "SkyPulse",
                style = MaterialTheme.typography.displayLarge,
                color = PureWhite,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Ride • Food • Grocery • Parcel • Transport",
                style = MaterialTheme.typography.titleMedium,
                color = LightSkyBlue
            )

            Spacer(Modifier.height(22.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Hyperlocal 3D", "All-in-One", "Gemini AI").forEach { tag ->
                    Surface(
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = LightPink,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
