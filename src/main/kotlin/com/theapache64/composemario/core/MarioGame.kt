package com.theapache64.composemario.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.MarioGame.Companion.BRICK_START_Y
import com.theapache64.composemario.core.MarioGame.Companion.CLOUD_SPEED
import com.theapache64.composemario.core.MarioGame.Companion.JUMP_SPEED
import com.theapache64.composemario.core.MarioGame.Companion.MARIO_SPEED
import com.theapache64.composemario.core.base.Game
import com.theapache64.composemario.models.Cloud
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.models.GameFrame
import com.theapache64.composemario.models.Mario

class MarioGame : Game {

    companion object {

        const val MARIO_SPEED = 15
        const val CLOUD_SPEED = 5
        const val LEVEL_LENGTH = WINDOW_WIDTH * 5 // 10 times the screen

        /**
         * 30% bricks so 70% play area. this may become dynamic as we build more levels
         */
        const val BRICK_START_Y = (WINDOW_HEIGHT * 0.70).toInt()
        const val MAX_JUMP_HEIGHT = (WINDOW_HEIGHT * 0.30)
        const val JUMP_SPEED = FloorBrick.BRICK_HEIGHT
    }


    private val _gameFrame: MutableState<GameFrame> by lazy {

        mutableStateOf(
            // First frame
            GameFrame(
                mario = Mario(action = Mario.START_ACTION, dstOffset = Mario.START_OFFSET),
                floorBricks = FloorBrick.createFloorBricks(), // building floor bricks
                clouds = Cloud.createClouds(),
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

            val newDirection = if (direction == Direction.UP && newMario.dstOffset.y <= MAX_JUMP_HEIGHT) {
                Direction.DOWN
            } else if (direction == Direction.DOWN && newMario.dstOffset.y >= (BRICK_START_Y - FloorBrick.BRICK_HEIGHT)) {
                Direction.IDLE_RIGHT
            } else {
                direction
            }

            val newClouds = clouds.step(direction)

            copy(
                mario = newMario,
                floorBricks = newBricks,
                isGameOver = isGameOver,
                direction = newDirection,
                clouds = newClouds
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

private fun List<Cloud>.step(direction: Direction): List<Cloud> {
    return when (direction) {
        Direction.MOVE_RIGHT -> {
            map { cloud ->
                cloud.copy(x = cloud.x - CLOUD_SPEED)
            }
        }
        else -> this
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

    val shouldGoDown = if (direction != Direction.UP) {
        if (footBrick != null) {
            // println("Has brick! @$footBrick foot @$marioFootY")
            false
        } else {
            println("No brick below $marioFootY")
            true
        }
    } else {
        false
    }

    val newY = if (shouldGoDown) {
        dstOffset.y + FloorBrick.BRICK_HEIGHT
    } else {
        // Go down
        when (direction) {
            Direction.UP -> {
                dstOffset.y - JUMP_SPEED
            }
            Direction.DOWN -> {
                var newY = dstOffset.y + JUMP_SPEED
                if (newY >= BRICK_START_Y) {
                    newY = BRICK_START_Y - FloorBrick.BRICK_HEIGHT
                }
                newY
            }
            else -> {
                dstOffset.y
            }
        }
    }


    val newX = if (marioXPercentage <= Mario.PUSH_PERCENTAGE && direction == Direction.MOVE_RIGHT) {
        println("Mario moves")
        dstOffset.x + MARIO_SPEED
    } else {
        when (direction) {
            Direction.MOVE_LEFT -> {
                println("Mario moving left")
                val nx = dstOffset.x - MARIO_SPEED
                if (nx >= 0) {
                    nx
                } else {
                    dstOffset.x
                }
            }
            else -> dstOffset.x
        }
    }


    val newAction = when (direction) {
        Direction.UP -> {
            when (action) {
                Mario.Action.SMALL_LOOK_RIGHT -> Mario.Action.SMALL_JUMP_RIGHT
                Mario.Action.SMALL_LOOK_LEFT -> Mario.Action.SMALL_JUMP_LEFT
                else -> action
            }
        }
        Direction.MOVE_RIGHT -> {
            when (action) {
                Mario.Action.SMALL_WALK_LEFT_BRAKE -> Mario.Action.SMALL_WALK_RIGHT_1
                Mario.Action.SMALL_LOOK_RIGHT -> Mario.Action.SMALL_WALK_RIGHT_1
                Mario.Action.SMALL_WALK_RIGHT_1 -> Mario.Action.SMALL_WALK_RIGHT_2
                Mario.Action.SMALL_WALK_RIGHT_2 -> Mario.Action.SMALL_WALK_RIGHT_3
                Mario.Action.SMALL_WALK_RIGHT_3 -> Mario.Action.SMALL_WALK_RIGHT_1

                // If he was running left, then brake
                Mario.Action.SMALL_WALK_LEFT_1,
                Mario.Action.SMALL_WALK_LEFT_2,
                Mario.Action.SMALL_WALK_LEFT_3,
                    // -> Mario.Action.SMALL_WALK_LEFT_BRAKE
                -> Mario.Action.SMALL_WALK_RIGHT_BRAKE // FIXME: Brake is too fast


                else -> Mario.Action.SMALL_LOOK_RIGHT // TODO
            }
        }
        Direction.MOVE_LEFT -> {
            when (action) {
                Mario.Action.SMALL_WALK_RIGHT_BRAKE -> Mario.Action.SMALL_WALK_LEFT_1
                Mario.Action.SMALL_LOOK_LEFT -> Mario.Action.SMALL_WALK_LEFT_1
                Mario.Action.SMALL_WALK_LEFT_1 -> Mario.Action.SMALL_WALK_LEFT_2
                Mario.Action.SMALL_WALK_LEFT_2 -> Mario.Action.SMALL_WALK_LEFT_3
                Mario.Action.SMALL_WALK_LEFT_3 -> Mario.Action.SMALL_WALK_LEFT_1

                // If he was running right, then brake
                Mario.Action.SMALL_WALK_RIGHT_1,
                Mario.Action.SMALL_WALK_RIGHT_2,
                Mario.Action.SMALL_WALK_RIGHT_3,
                -> Mario.Action.SMALL_WALK_LEFT_BRAKE // FIXME: Brake is too fast

                else -> Mario.Action.SMALL_LOOK_LEFT // TODO
            }
        }
        Direction.IDLE_RIGHT -> Mario.Action.SMALL_LOOK_RIGHT
        Direction.IDLE_LEFT -> Mario.Action.SMALL_LOOK_LEFT
        else -> {
            // TODO : Handle more directions
            action
        }
    }

    return copy(
        dstOffset = IntOffset(newX, newY),
        action = newAction,
    )
}

private fun List<FloorBrick>.step(
    mario: Mario,
    direction: Direction,
): List<FloorBrick> {
    /**
     * Move bricks only when we talk right
     */
    return when (direction) {
        Direction.MOVE_RIGHT -> {
            val marioYPercentage = (mario.dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
            println("MarioAt : $marioYPercentage")
            if (marioYPercentage >= Mario.PUSH_PERCENTAGE) {
                // Mario moved more than push percentage, now let's move the bricks
                map { brick ->
                    brick.copy(x = brick.x - MARIO_SPEED)
                }
            } else {
                this
            }
        }
        else -> this
    }
}
