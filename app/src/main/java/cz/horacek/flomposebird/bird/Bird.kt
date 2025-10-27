package cz.horacek.flomposebird.bird

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cz.horacek.flomposebird.pipe.PipeData
import cz.horacek.flomposebird.pipe.PipeRect

@Composable
fun Bird(modifier: Modifier = Modifier, bird: BirdData) {
    with(LocalDensity.current) {
        Canvas(
            modifier = modifier
                .size(bird.width.toDp(), bird.height.toDp())
                .offset { IntOffset(bird.x, bird.y.value) }
                .border(1.dp, Color.Red)
        ) {
            drawCircle(color = Color.Yellow)
        }
    }
}

data class BirdData(
    val x: Int,
    val y: MutableState<Int>,
    val width: Int = 150,
    val height: Int = 150
) {
    private fun isWithinPipeRect(pipeRect: PipeRect): Boolean {
        return (x + width >= pipeRect.left && x <= pipeRect.right) && (y.value + height >= pipeRect.top && y.value <= pipeRect.bottom)
    }

    fun isWithinPipe(pipe: PipeData?): Boolean {
        if (pipe == null) return false

        return isWithinPipeRect(pipe.getTopPipeRect()) || isWithinPipeRect(pipe.getBottomPipeRect())
    }

    fun isPastThePipe(pipeData: PipeData?): Boolean {
        val isPast = ((pipeData?.currentX?.value?.plus(pipeData.topPipePart.width)) ?: 0) < x
        Log.d("Birb", "isPast: $isPast")
        return isPast
    }


    fun moveBird(by: Int = BIRD_LIFT_VALUE) {
        y.value += by
    }

    companion object {
        private const val BIRD_LIFT_VALUE = -230
    }
}