
package com.example.fit5046a3a4.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            rotate(angle) {
                drawCircle(
                    color = primary.copy(alpha = 0.1f),
                    radius = size.maxDimension * 0.7f,
                    center = Offset(size.width * 0.2f, size.height * 0.2f)
                )
                drawCircle(
                    color = secondary.copy(alpha = 0.1f),
                    radius = size.maxDimension * 0.5f,
                    center = Offset(size.width * 0.8f, size.height * 0.3f)
                )
                drawCircle(
                    color = tertiary.copy(alpha = 0.1f),
                    radius = size.maxDimension * 0.6f,
                    center = Offset(size.width * 0.5f, size.height * 0.8f)
                )
            }
        }
        content()
    }
}

@Composable
fun FloatingBubbles(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    
    val infiniteTransition = rememberInfiniteTransition()
    val bubblePositions = remember { List(5) { Pair(Math.random(), Math.random()) } }
    
    val animations = bubblePositions.map { (initX, initY) ->
        val xAnim by infiniteTransition.animateFloat(
            initialValue = (initX * 100).toFloat(),
            targetValue = (initX * 100 + 50).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(3000 + (Math.random() * 2000).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val yAnim by infiniteTransition.animateFloat(
            initialValue = (initY * 100).toFloat(),
            targetValue = (initY * 100 + 50).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(4000 + (Math.random() * 2000).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Pair(xAnim, yAnim)
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            animations.forEachIndexed { index, (x, y) ->
                drawCircle(
                    color = if (index % 2 == 0) primary else secondary,
                    radius = 20f + (index * 5),
                    alpha = 0.1f,
                    center = Offset(
                        x = (x / 100f) * size.width,
                        y = (y / 100f) * size.height
                    )
                )
            }
        }
        content()
    }
}