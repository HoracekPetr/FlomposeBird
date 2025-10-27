package cz.horacek.flomposebird.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.horacek.flomposebird.R
import cz.horacek.flomposebird.bird.Bird
import cz.horacek.flomposebird.pipe.Pipe

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val window = LocalWindowInfo.current

    val screenHeight = window.containerSize.height
    val screenWidth = window.containerSize.width

    val interactionSource = remember { MutableInteractionSource() }

    val firstPipe by viewModel.firstPipe.collectAsStateWithLifecycle()
    val secondPipe by viewModel.secondPipe.collectAsStateWithLifecycle()
    val bird by viewModel.bird.collectAsStateWithLifecycle()
    val score by viewModel.gameScore.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init(height = screenHeight, width = screenWidth)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { viewModel.liftBird() }
            ))
    {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.game_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = "Score: $score",
            color = Color.White,
            fontSize = 16.sp
        )

        firstPipe?.let {
            Pipe(
                pipeData = it
            )
        }
        secondPipe?.let {
            Pipe(
                pipeData = it
            )
        }

        bird?.let {
            Bird(bird = it)
        }
    }
}

