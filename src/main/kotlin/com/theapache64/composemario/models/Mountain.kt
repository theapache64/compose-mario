package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame

data class Mountain(
    val x: Int,
    val y: Int,
    val type: Type,
) {

    companion object {

        /**
         * Here, Int is the level percentage
         */
        private val mountainMap = mutableMapOf(
            // These are mandatory clouds
            2 to Type.SMALL, // means when 2% of the level reaches, there should a small cloud
            5 to Type.SMALL,

            10 to Type.LARGE,
            11 to Type.SMALL,

            13 to Type.SMALL,
            20 to Type.LARGE,
            24 to Type.LARGE,
            30 to Type.SMALL,

            35 to Type.SMALL,
            42 to Type.LARGE,
            48 to Type.LARGE,
            50 to Type.LARGE,
            55 to Type.SMALL,
            62 to Type.LARGE,
            63 to Type.SMALL,
            67 to Type.LARGE,
            70 to Type.LARGE,
            72 to Type.SMALL,
            76 to Type.SMALL,
            78 to Type.LARGE,

            81 to Type.LARGE,
            84 to Type.SMALL,
            91 to Type.SMALL,
        )


        fun createMountains(): List<Mountain> {
            return mutableListOf<Mountain>().apply {
                for ((percentage, mountainType) in mountainMap) {
                    val mx = (MarioGame.LEVEL_LENGTH * percentage) / 100f
                    println(mx)
                    val my = MarioGame.BRICK_START_Y - mountainType.dstSize.height
                    add(
                        Mountain(
                            mx.toInt(),
                            my,
                            mountainType
                        )
                    )
                }
            }
        }


        fun List<Mountain>.filterVisibleMountains(): List<Mountain> {
            return this.filter { mountain ->
                mountain.x in -(mountain.type.dstSize.width)..WINDOW_WIDTH && mountain.y in 0..WINDOW_HEIGHT
            }
        }

        fun List<Mountain>.stepMountains(directions: Set<Direction>, mario: Mario): List<Mountain> {

            return if (directions.contains(Direction.MOVE_RIGHT)) {
                if (mario.shouldMoveOtherObjects()) {
                    map { mountain ->
                        mountain.copy(x = mountain.x - MarioGame.MOUNTAIN_SPEED)
                    }
                } else {
                    this
                }
            } else {
                this
            }


        }
    }

    enum class Type(
        val srcOffset: IntOffset,
        val srcSize: IntSize,
        val dstSize: IntSize = srcSize * 2,
    ) {
        LARGE(
            srcOffset = IntOffset(86, 5),
            srcSize = IntSize(80, 35)
        ),

        SMALL(
            srcOffset = IntOffset(169, 21),
            srcSize = IntSize(48, 19)
        ),
    }
}