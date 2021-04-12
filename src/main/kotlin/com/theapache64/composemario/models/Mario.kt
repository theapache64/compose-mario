package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
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
        val START_OFFSET = IntOffset(20, MarioGame.BRICK_START_Y - START_ACTION.dstSize.height)

        const val MAX_JUMP_HEIGHT = (WINDOW_HEIGHT * 0.30)
        const val JUMP_SPEED = FloorBrick.BRICK_HEIGHT

        fun Mario.stepMario(
            directions: Set<Direction>,
            bricks: List<FloorBrick>,
        ): Mario {

            // TODO: Calculate Y to support jump, gravity and fly

            // Calculating X coordinate
            val marioXPercentage = (dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
            val newX = if (marioXPercentage <= PUSH_PERCENTAGE && directions.contains(Direction.MOVE_RIGHT)) {
                dstOffset.x + MarioGame.MARIO_SPEED
            } else {
                if (directions.contains(Direction.MOVE_LEFT)) {
                    val nx = dstOffset.x - MarioGame.MARIO_SPEED
                    if (nx >= 0) {
                        nx
                    } else {
                        dstOffset.x
                    }
                } else {
                    dstOffset.x
                }
            }

            val newAction = when {
                directions.contains(Direction.MOVE_RIGHT) -> {
                    when (action) {
                        Action.SMALL_WALK_LEFT_BRAKE -> Action.SMALL_WALK_RIGHT_1
                        Action.SMALL_LOOK_RIGHT -> Action.SMALL_WALK_RIGHT_1
                        Action.SMALL_WALK_RIGHT_1 -> Action.SMALL_WALK_RIGHT_2
                        Action.SMALL_WALK_RIGHT_2 -> Action.SMALL_WALK_RIGHT_3
                        Action.SMALL_WALK_RIGHT_3 -> Action.SMALL_WALK_RIGHT_1

                        // If he was running left, then brake
                        Action.SMALL_WALK_LEFT_1,
                        Action.SMALL_WALK_LEFT_2,
                        Action.SMALL_WALK_LEFT_3,
                            // -> Mario.Action.SMALL_WALK_LEFT_BRAKE
                        -> Action.SMALL_WALK_RIGHT_BRAKE // FIXME: Brake is too fast


                        else -> Action.SMALL_LOOK_RIGHT // TODO
                    }
                }

                directions.contains(Direction.MOVE_LEFT) -> {
                    when (action) {
                        Action.SMALL_WALK_RIGHT_BRAKE -> Action.SMALL_WALK_LEFT_1
                        Action.SMALL_LOOK_LEFT -> Action.SMALL_WALK_LEFT_1
                        Action.SMALL_WALK_LEFT_1 -> Action.SMALL_WALK_LEFT_2
                        Action.SMALL_WALK_LEFT_2 -> Action.SMALL_WALK_LEFT_3
                        Action.SMALL_WALK_LEFT_3 -> Action.SMALL_WALK_LEFT_1

                        // If he was running right, then brake
                        Action.SMALL_WALK_RIGHT_1,
                        Action.SMALL_WALK_RIGHT_2,
                        Action.SMALL_WALK_RIGHT_3,
                        -> Action.SMALL_WALK_LEFT_BRAKE // FIXME: Brake is too fast

                        else -> Action.SMALL_LOOK_LEFT // TODO
                    }
                }

                directions.contains(Direction.IDLE_RIGHT) -> {
                    Action.SMALL_LOOK_RIGHT
                }

                directions.contains(Direction.IDLE_LEFT) -> {
                    Action.SMALL_LOOK_LEFT
                }

                else -> {
                    action
                }
            }

            return copy(
                dstOffset = IntOffset(newX, dstOffset.y),
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

    fun shouldMoveOtherObjects(): Boolean {
        val marioYPercentage = (dstOffset.x / WINDOW_WIDTH.toFloat()) * 100
        println("MarioAt : $marioYPercentage")
        return marioYPercentage >= PUSH_PERCENTAGE
    }
}