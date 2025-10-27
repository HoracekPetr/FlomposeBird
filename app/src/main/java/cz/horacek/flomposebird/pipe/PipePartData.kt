package cz.horacek.flomposebird.pipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import cz.horacek.flomposebird.R

@Composable
fun Pipe(
    modifier: Modifier = Modifier,
    pipeData: PipeData
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .offset { IntOffset(x = pipeData.currentX.value, y = 0) }
    ) {
        PipePart(
            pipePartData = pipeData.topPipePart,
            isBottom = false
        )
        PipePart(
            pipePartData = pipeData.bottomPipePart,
            isBottom = true
        )
    }
}

@Composable
private fun PipePart(
    modifier: Modifier = Modifier,
    pipePartData: PipePartData,
    isBottom: Boolean
) {
    with(LocalDensity.current) {
        Column(
            modifier = modifier
                .width(pipePartData.width.toDp())
                .height(pipePartData.height.toDp())
                .offset {
                    IntOffset(0, pipePartData.y)
                }
        ) {
            if (isBottom) {
                Image(
                    modifier = modifier,
                    painter = painterResource(id = R.drawable.pipe_top),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }

            Image(
                modifier = modifier
                    .weight(1f),
                painter = painterResource(id = R.drawable.pipe_body),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )

            if (!isBottom) {
                Image(
                    modifier = modifier,
                    painter = painterResource(id = R.drawable.pipe_top),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }
        }
    }
}

data class PipeData(
    var currentX: MutableState<Int>,
    val topPipePart: PipePartData,
    val bottomPipePart: PipePartData,
    val points: Int = 1
) {
    private fun getPipeRect(x: Int, y: Int, width: Int, height: Int): PipeRect {
        return PipeRect(
            left = x,
            right = x + width,
            top = y,
            bottom = y + height
        )
    }

    private fun getPipePartRect(pipePartData: PipePartData) = getPipeRect(
        x = currentX.value,
        y = pipePartData.y,
        width = pipePartData.width,
        height = pipePartData.height
    )

    fun moveXLeft(newX: Int = 5) {
        currentX.value -= newX
    }

    fun getTopPipeRect() = getPipePartRect(topPipePart)
    fun getBottomPipeRect() = getPipePartRect(bottomPipePart)
}

data class PipePartData(
    val y: Int,
    val width: Int,
    val height: Int
)

data class PipeRect(
    val left: Int,
    val right: Int,
    val top: Int,
    val bottom: Int
)
