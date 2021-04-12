package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame

data class Tube(
    val x: Int,
    val y: Int,
    val type: Type,
) {

    companion object {

        /**
         * Here, Int is the level percentage
         */
        private val tubesMap = mutableMapOf(
            // These are mandatory clouds
            // 10 to Type.SMALL,
            30 to Type.LARGE,
            40 to Type.SMALL,

            60 to Type.LARGE,
            80 to Type.LARGE,
        )


        fun createTubes(): List<Tube> {
            return mutableListOf<Tube>().apply {
                for ((percentage, tubeType) in tubesMap) {
                    val tubeX = (MarioGame.LEVEL_LENGTH * percentage) / 100f
                    val tubeY = MarioGame.BRICK_START_Y - tubeType.dstSize.height
                    add(
                        Tube(
                            tubeX.toInt(),
                            tubeY,
                            tubeType
                        )
                    )
                }
            }
        }

        fun List<Tube>.filterVisibleTubes(): List<Tube> {
            return this.filter { forest ->
                forest.x in -(forest.type.dstSize.width)..WINDOW_WIDTH && forest.y in 0..WINDOW_HEIGHT
            }
        }

        fun List<Tube>.stepTubes(direction: Set<Direction>, mario: Mario): List<Tube> {
            return when {
                direction.contains(Direction.MOVE_RIGHT) -> {
                    if (mario.shouldMoveOtherObjects()) {
                        map { tube ->
                            tube.copy(x = tube.x - MarioGame.TUBE_SPEED)
                        }
                    } else {
                        this
                    }
                }
                else -> this
            }
        }
    }

    enum class Type(
        val srcOffset: IntOffset,
        val srcSize: IntSize,
        val dstSize: IntSize = srcSize * 2,
    ) {

        SMALL(
            srcOffset = IntOffset(309, 417),
            srcSize = IntSize(32, 32)
        ),

        MEDIUM(
            srcOffset = IntOffset(271, 401),
            srcSize = IntSize(32, 48)
        ),

        LARGE(
            srcOffset = IntOffset(230, 385),
            srcSize = IntSize(32, 64)
        ),

        LARGE_L(
            srcOffset = IntOffset(156, 385),
            srcSize = IntSize(64, 64)
        ),

        EXTRA_LARGE_L(
            srcOffset = IntOffset(84, 321),
            srcSize = IntSize(62, 128)
        ),

    }
}