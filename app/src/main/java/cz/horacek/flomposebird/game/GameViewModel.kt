package cz.horacek.flomposebird.game

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.horacek.flomposebird.bird.BirdData
import cz.horacek.flomposebird.pipe.PipeData
import cz.horacek.flomposebird.pipe.PipePartData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.milliseconds

class GameViewModel : ViewModel() {
    private var gameDataInit = false

    private val _gameRunning = MutableStateFlow(true)
    val gameRunning = _gameRunning.asStateFlow()

    private var screenHeight: Int = 0
    private var screenWidth: Int = 0

    private val _firstPipe = MutableStateFlow<PipeData?>(null)
    val firstPipe = _firstPipe.asStateFlow()

    private val _secondPipe = MutableStateFlow<PipeData?>(null)
    val secondPipe = _secondPipe.asStateFlow()

    private val _bird = MutableStateFlow<BirdData?>(null)
    val bird = _bird.asStateFlow()

    private val _gameScore = MutableStateFlow(0)
    val gameScore = _gameScore.asStateFlow()

    fun init(height: Int, width: Int) {
        if (!gameDataInit) {
            screenHeight = height - GROUND_PADDING
            screenWidth = width

            startGame()
        }
    }

    fun startGame() {
        _gameRunning.update { true }

        _firstPipe.update { getPipe(screenWidth) }
        _secondPipe.update { getPipe(screenWidth + 800) }

        _bird.update { initBird() }

        _gameScore.update { 0 }

        gameDataInit = true

        viewModelScope.launch {
            while (_gameRunning.value) {
                delay(GAME_SPEED)

                applyGravityOnBird()

                movePipe(pipe = _firstPipe)
                movePipe(pipe = _secondPipe)

                checkForCollisions()
            }
        }
    }

    fun movePipe(pipe: MutableStateFlow<PipeData?>) {
        pipe.value?.moveXLeft()

        if ((pipe.value?.currentX?.value ?: 0) < -200) {
            pipe.update {
                getPipe(x = screenWidth + 300)
            }
        }

        if (_bird.value?.isPastThePipe(pipe.value) == true && (pipe.value?.points ?: 0) > 0) {
            _gameScore.update { _gameScore.value + 1 }
            pipe.update { pipe.value?.copy(points = 0) }
        }
    }

    fun applyGravityOnBird() {
        _bird.value?.moveBird(GRAVITY_FORCE_VALUE)
    }

    fun liftBird() {
        _bird.value?.moveBird()
    }

    private fun checkForCollisions() {
        val isBirdWithinFirstPipe = _bird.value?.isWithinPipe(_firstPipe.value) == true
        val isBirdWithinSecondPipe = _bird.value?.isWithinPipe(_secondPipe.value) == true

        if (isBirdWithinFirstPipe || isBirdWithinSecondPipe) {
            _gameRunning.update { false }
            Log.d("Birb", "BONK")
        }
    }

    private fun getPipe(x: Int): PipeData {
        val topHeight = Random.nextInt(200..(screenHeight * 0.75).toInt())
        val bottomY = topHeight + GAP_SIZE
        val bottomHeight = screenHeight - bottomY
        return PipeData(
            currentX = mutableIntStateOf(x),
            topPipePart = PipePartData(y = 0, width = 200, height = topHeight),
            bottomPipePart = PipePartData(
                y = bottomY,
                width = 200,
                height = bottomHeight
            )
        )
    }

    private fun initBird(): BirdData {
        return BirdData(
            x = 50,
            y = mutableIntStateOf(screenHeight / 2)
        )
    }

    companion object {
        private const val GRAVITY_FORCE_VALUE = 12
        private const val GROUND_PADDING = 180
        private const val GAP_SIZE = 600

        private val GAME_SPEED = 17.milliseconds // Simulate 60FPS
    }
}