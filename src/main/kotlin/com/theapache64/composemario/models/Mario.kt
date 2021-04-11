package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

data class Mario(
    val action: Action,
    val dstOffset: IntOffset,
) {

    companion object {
        /**
         * When mario reaches at this percentage of screen, the movement will switch to bricks
         */
        const val PUSH_PERCENTAGE = 25

        /**
         * Where life begins
         */
        val START_ACTION = Action.SMALL_LOOK_RIGHT
        val START_OFFSET = IntOffset(20, 0)
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
            srcOffset = IntOffset(331, 0),
            srcSize = IntSize(17, 16)
        ),

    }
}