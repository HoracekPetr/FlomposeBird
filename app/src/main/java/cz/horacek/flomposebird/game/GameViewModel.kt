package cz.horacek.flomposebird.game

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

    private var gameRunning = true

    private var screenHeight: Int = 0
    private var screenWidth: Int = 0

    private val GAME_SPEED = 17.milliseconds // Simulate 60FPS

    private val GAP_SIZE = 600

    private val _firstPipe = MutableStateFlow<PipeData?>(null)
    val firstPipe = _firstPipe.asStateFlow()

    private val _secondPipe = MutableStateFlow<PipeData?>(null)
    val secondPipe = _secondPipe.asStateFlow()

    private val _bird = MutableStateFlow<BirdData?>(null)
    val bird = _bird.asStateFlow()

    fun init(height: Int, width: Int) {
        if (!gameDataInit) {
            screenHeight = height
            screenWidth = width


            _firstPipe.update { getPipe(screenWidth) }
            _secondPipe.update { getPipe(screenWidth + 800) }

            _bird.update { initBird() }

            gameDataInit = true

            startGame()
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            while (gameRunning) {
                delay(GAME_SPEED)

                _bird.update {
                    it?.copy(y = it.y + 12)
                }

                _firstPipe.update {
                    it?.copy(
                        currentX = it.currentX - 5
                    )
                }

                _secondPipe.update {
                    it?.copy(
                        currentX = it.currentX - 5
                    )
                }

                if ((_firstPipe.value?.currentX ?: 0) < -200) {
                    _firstPipe.update {
                        getPipe(x = screenWidth + 300)
                    }
                }

                if ((_secondPipe.value?.currentX ?: 0) < -200) {
                    _secondPipe.update {
                        getPipe(x = screenWidth + 300)
                    }
                }

                if(_bird.value?.x == _firstPipe.value?.currentX && _bird.value?.y in 300..800) {
                    gameRunning = false
                }
            }
        }
    }

    fun liftBird() {
        _bird.update {
            it?.copy(y = it.y - 200)
        }
    }

    private fun getPipe(x: Int): PipeData {
        val topHeight = Random.nextInt(200..(screenHeight * 0.75).toInt())
        val bottomY = topHeight + GAP_SIZE
        val bottomHeight = screenHeight - bottomY
        return PipeData(
            currentX = x,
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
            y = screenHeight / 2
        )
    }
}