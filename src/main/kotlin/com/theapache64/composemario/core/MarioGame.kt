package com.theapache64.composemario.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.base.Game
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.models.GameFrame
import com.theapache64.composemario.models.Mario
import kotlin.math.ceil

class MarioGame : Game {

    companion object {

        private const val LEVEL_LENGTH = WINDOW_WIDTH * 10 // 10 times the screen

        // Brick
        const val BRICK_WIDTH = 30
        const val BRICK_HEIGHT = 30

        const val MIN_Y = (WINDOW_HEIGHT * 0.70).toInt()
    }

    private val _gameFrame: MutableState<GameFrame>

    init {

        // First frame
        _gameFrame = mutableStateOf(
            GameFrame(
                mario = Mario(action = Mario.START_ACTION, dstOffset = Mario.START_OFFSET),
                floorBricks = createFloorBricks(),
                goombas = listOf(),
                direction = Direction.IDLE_RIGHT // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        update {
            copy(
                mario = mario.step(direction),
                floorBricks = floorBricks.step(mario, direction)
            )
        }
    }

    override fun setDirection(direction: Direction) {
        update {
            copy(
                direction = direction
            )
        }
    }

    /**
     * To create floor bricks
     */
    private fun createFloorBricks(): List<FloorBrick> {

        val floorBricks = mutableListOf<FloorBrick>()

        val brickColumns = ceil(LEVEL_LENGTH / BRICK_WIDTH.toFloat()).toInt()
        val brickRows = (WINDOW_HEIGHT - MIN_Y) / BRICK_HEIGHT

        repeat(brickRows) { rowIndex ->
            repeat(brickColumns) { columnIndex ->
                val bx = (BRICK_WIDTH * columnIndex)
                val by = MIN_Y + (BRICK_HEIGHT * rowIndex)
                print("[$bx, $by] ")
                val floorBrick = FloorBrick(bx, by)
                floorBricks.add(floorBrick)
            }
            println(" ")
        }
        println("Total Floor Bricks : ${floorBricks.size}")

        return floorBricks
    }

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}

private fun Mario.step(
    direction: Direction,
): Mario {
    val marioAt = (dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
    return if (marioAt <= Mario.PUSH_PERCENTAGE) {
        copy(
            dstOffset = when (direction) {
                Direction.MOVE_RIGHT -> {
                    dstOffset.copy(x = dstOffset.x + 10)
                }
                else -> dstOffset
            }
        )
    } else {
        copy()
    }
}

private fun List<FloorBrick>.step(
    mario: Mario,
    direction: Direction,
): List<FloorBrick> {
    return when (direction) {
        Direction.IDLE_LEFT -> {
            this
        }
        Direction.IDLE_RIGHT -> {
            this
        }
        Direction.MOVE_LEFT -> {
            map { brick ->
                brick.copy(x = brick.x + 10)
            }
        }
        Direction.MOVE_RIGHT -> {
            val marioAt = (mario.dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
            println("MarioAt : $marioAt")
            if (marioAt >= Mario.PUSH_PERCENTAGE) {
                println("Mario at max")
                map { brick ->
                    brick.copy(x = brick.x - 10)
                }
            } else {
                this
            }
        }
        Direction.UP -> {
            this
        }
        Direction.DOWN -> {
            this
        }
    }
}
