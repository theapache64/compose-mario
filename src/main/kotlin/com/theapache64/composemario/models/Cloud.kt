package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame

data class Cloud(
    val x: Int,
    val y: Int,
    val type: Type,
) {

    companion object {

        /**
         * Here, Int is the level percentage
         */
        private val cloudMap = mutableMapOf(
            // These are mandatory clouds
            2 to Type.SMALL, // means when 2% of the level reaches, there should a small cloud
            5 to Type.SMALL,
            10 to Type.LARGE,
            12 to Type.SMALL,
            15 to Type.SMALL,
            23 to Type.LARGE,
            36 to Type.SMALL,
            43 to Type.LARGE,
            54 to Type.SMALL,
            65 to Type.LARGE,
            70 to Type.LARGE,
            73 to Type.SMALL,
            84 to Type.LARGE,
            90 to Type.SMALL,
            93 to Type.SMALL,
            98 to Type.LARGE,
        ).apply {
            // These are random clouds
            val percentageRange = 0..100
            repeat(30) {
                put(percentageRange.random(), Type.values().random())
            }

        }


        fun createClouds(): List<Cloud> {
            return mutableListOf<Cloud>().apply {
                val yRange = 0..(WINDOW_HEIGHT * 0.25f).toInt()

                for ((percentage, cloudType) in cloudMap) {
                    val cloudX = (MarioGame.LEVEL_LENGTH * percentage) / 100f
                    val cloudY = yRange.random()
                    add(
                        Cloud(
                            cloudX.toInt(),
                            cloudY,
                            cloudType
                        )
                    )
                }
            }
        }

        fun List<Cloud>.filterVisibleClouds(): List<Cloud> {
            return this.filter { cloud ->
                cloud.x in -(cloud.type.dstSize.width)..WINDOW_WIDTH && cloud.y in 0..WINDOW_HEIGHT
            }
        }

        fun List<Cloud>.stepClouds(direction: Set<Direction>, mario: Mario): List<Cloud> {
            return when {
                direction.contains(Direction.MOVE_RIGHT) -> {
                    if (mario.shouldMoveOtherObjects()) {
                        map { cloud ->
                            cloud.copy(x = cloud.x - MarioGame.CLOUD_SPEED)
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
        LARGE(
            srcOffset = IntOffset(144, 69),
            srcSize = IntSize(64, 24)
        ),

        SMALL(
            srcOffset = IntOffset(211, 69),
            srcSize = IntSize(32, 24)
        ),
    }
}