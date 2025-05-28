package cz.horacek.flomposebird.pipe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset

@Composable
fun Pipe(
    modifier: Modifier = Modifier,
    pipeData: PipeData
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .offset { IntOffset(x = pipeData.currentX, y = 0) }
    ) {
        PipePart(
            pipePartData = pipeData.topPipePart
        )
        PipePart(
            pipePartData = pipeData.bottomPipePart,
        )
    }
}

@Composable
private fun PipePart(
    modifier: Modifier = Modifier,
    pipePartData: PipePartData,
) {
    with(LocalDensity.current) {
        Canvas(
            modifier = modifier
                .width(pipePartData.width.toDp())
                .height(pipePartData.height.toDp())
                .offset {
                    IntOffset(0, pipePartData.y)
                }
        ) {
            drawRect(Color.Green)
        }
    }
}

data class PipeData(
    val currentX: Int,
    val topPipePart: PipePartData,
    val bottomPipePart: PipePartData
)

data class PipePartData(
    val y: Int,
    val width: Int,
    val height: Int
)
