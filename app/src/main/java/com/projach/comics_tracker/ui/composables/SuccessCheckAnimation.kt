package com.projach.comics_tracker.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SuccessCheckAnimation(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {
    val scale = remember { Animatable(0f) }
    val checkProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        checkProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
        onFinished()
    }

    Canvas(modifier = modifier.size(120.dp)) {
        val w = size.width
        val h = size.height

        drawCircle(
            color = Color(0xFF4CAF50),
            radius = (w / 2) * scale.value
        )

        val checkPath = Path().apply {
            moveTo(w * 0.28f, h * 0.52f)
            lineTo(w * 0.44f, h * 0.68f)
            lineTo(w * 0.74f, h * 0.36f)
        }

        val measure = PathMeasure().apply { setPath(checkPath, false) }
        val partial = Path()
        measure.getSegment(0f, measure.length * checkProgress.value, partial, true)

        drawPath(
            path = partial,
            color = Color.White,
            style = Stroke(
                width = w * 0.09f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}