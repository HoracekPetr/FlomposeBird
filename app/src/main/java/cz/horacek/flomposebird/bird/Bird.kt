package cz.horacek.flomposebird.bird

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset

@Composable
fun Bird(modifier: Modifier = Modifier, bird: BirdData) {
    with(LocalDensity.current) {

        Canvas(
            modifier = Modifier
                .size(bird.width.toDp(), bird.height.toDp())
                .offset { IntOffset(bird.x, bird.y) }
        ) {
            drawCircle(color = Color.Yellow)
        }
    }
}

data class BirdData(
    val x: Int,
    val y: Int,
    val width: Int = 150,
    val height: Int = 150
)