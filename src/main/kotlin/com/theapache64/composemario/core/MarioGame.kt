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
import com.theapache64.composemario.theme.OrangeRoughy
import kotlin.math.ceil

class MarioGame : Game {

    companion object {

        private const val LEVEL_LENGTH = WINDOW_WIDTH * 2 // 10 times the screen

        // Brick
        const val BRICK_WIDTH = 30
        const val BRICK_HEIGHT = 30
        private val FLOOR_BRICK_COLOR = OrangeRoughy

        const val MIN_Y = (WINDOW_HEIGHT * 0.70).toInt()
    }

    private val _gameFrame: MutableState<GameFrame>

    init {

        // First frame
        _gameFrame = mutableStateOf(
            GameFrame(
                mario = Mario(20, MIN_Y - 60),
                floorBricks = createFloorBricks(),
                goombas = listOf(),
                direction = Direction.IDLE_RIGHT // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        update {
            copy()
        }
    }

    override fun move(direction: Direction) {
        update {
            when (direction) {
                Direction.IDLE_LEFT -> {
                    copy()
                }
                Direction.IDLE_RIGHT -> {
                    copy()
                }
                Direction.MOVE_LEFT -> {
                    val newBrickList = floorBricks.map { brick ->
                        brick.x = brick.x + 10
                        brick
                    }

                    println(newBrickList)

                    copy(
                        floorBricks = newBrickList
                    )
                }
                Direction.MOVE_RIGHT -> {
                    println("Moving right two")
                    val newBrickList = floorBricks.map { brick ->
                        brick.x = brick.x - 10
                        brick.copy()
                    }

                    println(newBrickList)

                    copy(
                        floorBricks = newBrickList
                    )
                }
                Direction.UP -> TODO()
                Direction.DOWN -> TODO()
            }
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
        println("Updated")
        _gameFrame.value = _gameFrame.value.func()
    }
}