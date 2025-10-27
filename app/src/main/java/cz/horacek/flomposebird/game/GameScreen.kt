package cz.horacek.flomposebird.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
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
    val gameRunning by viewModel.gameRunning.collectAsStateWithLifecycle()
    val bird by viewModel.bird.collectAsStateWithLifecycle()
    val gameScore by viewModel.gameScore.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init(height = screenHeight, width = screenWidth)
    }

    Box {
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

            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "SKÓRE: $gameScore",
                color = Color.White,
                fontSize = 32.sp
            )

            bird?.let {
                Bird(bird = it)
            }
        }

        if (!gameRunning) {
            EndScreen(
                gameScore = gameScore,
                onRestart = viewModel::startGame
            )
        }
    }
}

@Composable
fun EndScreen(
    gameScore: Int,
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PROHRÁLS",
                color = Color.White,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SKÓRE: $gameScore",
                color = Color.White,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = onRestart,
                content = {
                    Text(
                        text = "Restart",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            )
        }
    }
}
