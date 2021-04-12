package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame

data class Forest(
    val x: Int,
    val y: Int,
    val type: Type,
) {

    companion object {

        /**
         * Here, Int is the level percentage
         */
        private val forestMap = mutableMapOf(
            // These are mandatory clouds
            2 to Type.SMALL, // means when 2% of the level reaches, there should a small cloud
            5 to Type.SMALL,

            10 to Type.LARGE,
            12 to Type.SMALL,

            15 to Type.SMALL,
            23 to Type.LARGE,

            35 to Type.SMALL,
            42 to Type.SMALL,

            53 to Type.LARGE,
            58 to Type.SMALL,
            60 to Type.LARGE,

            80 to Type.LARGE,
            85 to Type.SMALL,
            92 to Type.SMALL,
        )


        fun createForests(): List<Forest> {
            return mutableListOf<Forest>().apply {
                for ((percentage, forestType) in forestMap) {
                    val forestX = (MarioGame.LEVEL_LENGTH * percentage) / 100f
                    val forestY = MarioGame.BRICK_START_Y - forestType.dstSize.height
                    add(
                        Forest(
                            forestX.toInt(),
                            forestY,
                            forestType
                        )
                    )
                }
            }
        }

        fun List<Forest>.filterVisibleForests(): List<Forest> {
            return this.filter { forest ->
                forest.x in -(forest.type.dstSize.width)..WINDOW_WIDTH && forest.y in 0..WINDOW_HEIGHT
            }
        }

        fun List<Forest>.stepForests(direction: Direction): List<Forest> {
            return when (direction) {
                Direction.MOVE_RIGHT -> {
                    map { forest ->
                        forest.copy(x = forest.x - MarioGame.FOREST_SPEED)
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
        LARGE(
            srcOffset = IntOffset(220, 24),
            srcSize = IntSize(64, 16)
        ),

        SMALL(
            srcOffset = IntOffset(288, 24),
            srcSize = IntSize(32, 16)
        ),
    }
}