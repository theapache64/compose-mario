package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame

data class Mario(
    val action: Action,
    val dstOffset: IntOffset,
) {

    companion object {
        /**
         * When mario reaches at this percentage of screen, the movement will switch to bricks
         */
        const val PUSH_PERCENTAGE = 50

        /**
         * Where life begins
         */
        val START_ACTION = Action.SMALL_LOOK_RIGHT
        val START_OFFSET = IntOffset(20, 0)

        fun Mario.stepMario(
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
                        dstOffset.y - MarioGame.JUMP_SPEED
                    }
                    Direction.DOWN -> {
                        var newY = dstOffset.y + MarioGame.JUMP_SPEED
                        if (newY >= MarioGame.BRICK_START_Y) {
                            newY = MarioGame.BRICK_START_Y - FloorBrick.BRICK_HEIGHT
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
                dstOffset.x + MarioGame.MARIO_SPEED
            } else {
                when (direction) {
                    Direction.MOVE_LEFT -> {
                        println("Mario moving left")
                        val nx = dstOffset.x - MarioGame.MARIO_SPEED
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
    }


    /**
     * All actions mario can do
     */
    enum class Action(
        val srcOffset: IntOffset,
        val srcSize: IntSize,
        val dstSize: IntSize = srcSize * 2,
    ) {

        BIG_LOOK_RIGHT(
            srcOffset = IntOffset(209, 52),
            srcSize = IntSize(16, 32),
        ),

        // SMALL - RIGHT
        SMALL_LOOK_RIGHT(
            srcOffset = IntOffset(211, 0),
            srcSize = IntSize(13, 16)
        ),

        SMALL_WALK_RIGHT_1(
            srcOffset = IntOffset(241, 0),
            srcSize = IntSize(14, 15)
        ),

        SMALL_WALK_RIGHT_2(
            srcOffset = IntOffset(272, 0),
            srcSize = IntSize(12, 16)
        ),

        SMALL_WALK_RIGHT_3(
            srcOffset = IntOffset(300, 0),
            srcSize = IntSize(16, 16)
        ),

        SMALL_WALK_RIGHT_BRAKE(
            srcOffset = IntOffset(331, 0),
            srcSize = IntSize(14, 16)
        ),

        SMALL_JUMP_RIGHT(
            srcOffset = IntOffset(359, 0),
            srcSize = IntSize(17, 16)
        ),

        // SMALL - LEFT
        SMALL_LOOK_LEFT(
            srcOffset = IntOffset(181, 0),
            srcSize = IntSize(13, 16)
        ),

        SMALL_WALK_LEFT_1(
            srcOffset = IntOffset(150, 0),
            srcSize = IntSize(14, 15)
        ),

        SMALL_WALK_LEFT_2(
            srcOffset = IntOffset(121, 0),
            srcSize = IntSize(12, 16)
        ),

        SMALL_WALK_LEFT_3(
            srcOffset = IntOffset(89, 0),
            srcSize = IntSize(16, 16)
        ),

        SMALL_WALK_LEFT_BRAKE(
            srcOffset = IntOffset(89, 0),
            srcSize = IntSize(16, 16)
        ),

        SMALL_JUMP_LEFT(
            srcOffset = IntOffset(29, 0),
            srcSize = IntSize(17, 16)
        ),

    }
}