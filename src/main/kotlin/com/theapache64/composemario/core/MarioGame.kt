package com.theapache64.composemario.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.base.Game
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.models.GameFrame
import com.theapache64.composemario.models.Mario

class MarioGame : Game {

    companion object {

        const val LEVEL_LENGTH = WINDOW_WIDTH * 0.5 // 10 times the screen

        /**
         * 30% bricks so 70% play area. this may become dynamic as we build more levels
         */
        const val BRICK_START_Y = (WINDOW_HEIGHT * 0.70).toInt()
    }


    private val _gameFrame: MutableState<GameFrame> by lazy {

        mutableStateOf(
            // First frame
            GameFrame(
                mario = Mario(action = Mario.START_ACTION, dstOffset = Mario.START_OFFSET),
                floorBricks = FloorBrick.createFloorBricks(), // building floor bricks
                goombas = listOf(), // TODO : I can't wait implement this xD
                direction = Direction.IDLE_RIGHT // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        println("Calling step")
        update {
            val newBricks = floorBricks.step(mario, direction)
            val newMario = mario.step(direction, newBricks)
            val isGameOver = newMario.dstOffset.y > WINDOW_HEIGHT
            copy(
                mario = newMario,
                floorBricks = newBricks,
                isGameOver = isGameOver
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

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}


private fun Mario.step(
    direction: Direction,
    bricks: List<FloorBrick>,
): Mario {
    val marioXPercentage = (dstOffset.x / WINDOW_WIDTH.toFloat()) * 100

    // Checking if mario have some
    val marioFootX = dstOffset.x - FloorBrick.BRICK_WIDTH
    val marioFootY = dstOffset.y + action.dstSize.height
    val footBrick = bricks.find { brick ->
        marioFootX in brick.x - FloorBrick.BRICK_WIDTH..brick.x && marioFootY in brick.y..brick.y + FloorBrick.BRICK_HEIGHT
    }

    val shouldGoDown = if (footBrick != null) {
        println("Has brick! @$footBrick foot @$marioFootY")
        false
    } else {
        println("No brick below $marioFootY")
        true
    }

    val newY = if (shouldGoDown) {
        dstOffset.y + FloorBrick.BRICK_HEIGHT
    } else {
        dstOffset.y
    }

    val newX = if (marioXPercentage <= Mario.PUSH_PERCENTAGE && direction == Direction.MOVE_RIGHT) {
        println("Mario moves")
        dstOffset.x + 10
    } else {
        when (direction) {
            Direction.MOVE_LEFT -> {
                println("Mario moving left")
                val nx = dstOffset.x - 10
                if (nx >= 0) {
                    nx
                } else {
                    dstOffset.x
                }
            }
            else -> dstOffset.x
        }
    }



    return copy(
        dstOffset = IntOffset(newX, newY)
    )
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
            this
        }
        Direction.MOVE_RIGHT -> {
            val marioYPercentage = (mario.dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
            println("MarioAt : $marioYPercentage")
            if (marioYPercentage >= Mario.PUSH_PERCENTAGE) {
                // Mario moved more than push percentage, now let's move the bricks
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
