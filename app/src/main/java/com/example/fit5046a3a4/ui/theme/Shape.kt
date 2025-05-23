
package com.example.fit5046a3a4.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),

    small = RoundedCornerShape(8.dp),

    medium = RoundedCornerShape(12.dp),

    large = RoundedCornerShape(16.dp),

    extraLarge = RoundedCornerShape(24.dp)
)

object CustomShapes {
    val TopRounded = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val BottomRounded = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )

    val Capsule = RoundedCornerShape(50)

    val AsymmetricRounded = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 24.dp,
        bottomStart = 24.dp,
        bottomEnd = 8.dp
    )

    val SoftRounded = RoundedCornerShape(8.dp)

    val EmphasisRounded = RoundedCornerShape(20.dp)

    val Dialog = RoundedCornerShape(28.dp)

    val FloatingButton = RoundedCornerShape(16.dp)

    val TextField = RoundedCornerShape(12.dp)

    val Card = RoundedCornerShape(16.dp)
}