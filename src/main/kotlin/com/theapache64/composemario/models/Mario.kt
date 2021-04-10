package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.core.MarioGame.Companion.BRICK_START_Y

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
        val START_OFFSET = IntOffset(20, BRICK_START_Y - START_ACTION.dstSize.height)
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

        SMALL_LOOK_RIGHT(
            srcOffset = IntOffset(211, 0),
            srcSize = IntSize(13, 16)
        )

    }
}