package com.theapache64.composemario.models

import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.core.MarioGame
import kotlin.math.ceil

data class FloorBrick(
    val x: Int,
    val y: Int,
) {
    companion object {
        // Brick
        const val BRICK_WIDTH = 30
        const val BRICK_HEIGHT = 30
        val BRICK_SIZE = IntSize(BRICK_WIDTH, BRICK_HEIGHT)

        /**
         * To create floor bricks
         */
        fun createFloorBricks(): List<FloorBrick> {

            val floorBricks = mutableListOf<FloorBrick>()

            val brickColumns = ceil(MarioGame.LEVEL_LENGTH / BRICK_WIDTH.toFloat()).toInt()
            val brickRows = (WINDOW_HEIGHT - MarioGame.BRICK_START_Y) / BRICK_HEIGHT

            repeat(brickRows) { rowIndex ->
                repeat(brickColumns) { columnIndex ->
                    val bx = (BRICK_WIDTH * columnIndex)
                    val by = MarioGame.BRICK_START_Y + (BRICK_HEIGHT * rowIndex)
                    print("[$bx, $by] ")
                    val floorBrick = FloorBrick(bx, by)
                    if (rowIndex == 0 && columnIndex == 5) {
                        // do nothing
                    } else {
                        floorBricks.add(floorBrick)
                    }
                }
                println(" ")
            }
            println("Total Floor Bricks : ${floorBricks.size}")

            return floorBricks
        }
    }
}