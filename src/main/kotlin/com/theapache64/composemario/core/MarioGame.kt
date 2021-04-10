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

class MarioGame : Game {

    companion object {

        const val LEVEL_LENGTH = WINDOW_WIDTH * 0.75 // 10 times the screen

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
                floorBricks =  FloorBrick.createFloorBricks(), // building floor bricks
                goombas = listOf(), // TODO : I can't wait implement this xD
                direction = Direction.IDLE_RIGHT // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        update {

            /*val newBricks = floorBricks.step(mario, direction).filter { brick ->
                brick.x in -FloorBrick.BRICK_WIDTH..WINDOW_WIDTH && brick.y in 0..WINDOW_HEIGHT
            }

            if (direction == Direction.MOVE_RIGHT || direction == Direction.IDLE_RIGHT) {
                // Checking if mario have some
                val marioFootX = mario.dstOffset.x - FloorBrick.BRICK_WIDTH
                val marioFootY = mario.dstOffset.y + mario.action.dstSize.height
                val footBrick = newBricks.find { brick -> brick.x > marioFootX && brick.y > marioFootY }
                if (footBrick != null) {
                    println("${floorBricks.size - newBricks.size} -> Has brick! @$footBrick foot @$marioFootY")
                } else {
                    println("No brick below $marioFootY")
                }
            }*/


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

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}


private fun Mario.step(
    direction: Direction,
): Mario {
    val marioYPercentage = (dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
    return if (marioYPercentage <= Mario.PUSH_PERCENTAGE) {

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
