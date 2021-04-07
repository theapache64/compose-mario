package com.theapache64.composemario

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.theme.CornflowerBlue
import com.theapache64.composemario.theme.OrangeRoughy
import kotlin.math.ceil

private const val WINDOW_WIDTH = 1020
private const val WINDOW_HEIGHT = 600

// Brick
private const val BRICK_WIDTH = 30
private const val BRICK_HEIGHT = 30
private val FLOOR_BRICK_COLOR = OrangeRoughy


fun getFloorBricks(): List<FloorBrick> {
    val floorBricks = mutableListOf<FloorBrick>()
    val minY = WINDOW_HEIGHT * 0.75
    val brickColumns = ceil(WINDOW_WIDTH / BRICK_WIDTH.toFloat()).toInt()
    val brickRows = (WINDOW_HEIGHT - minY.toInt()) / BRICK_HEIGHT
    repeat(brickRows) { rowIndex ->
        repeat(brickColumns) { columnIndex ->
            val bx = (BRICK_WIDTH * columnIndex)
            val by = minY.toInt() + (BRICK_HEIGHT * rowIndex)
            val floorBrick = FloorBrick(bx, by)
            floorBricks.add(floorBrick)
        }
    }
    return floorBricks
}


fun main() = Window(
    title = "Compose Mario",
    size = IntSize(WINDOW_WIDTH, WINDOW_HEIGHT)
) {

    val floorBricks = getFloorBricks()

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        // Sky
        drawRect(
            color = CornflowerBlue
        )

        // Floor
        for (floorBrick in floorBricks) {

            // Floor
            drawImage(
                image = imageFromResource("floor.png"),
                dstOffset = IntOffset(floorBrick.x, floorBrick.y),
                dstSize = IntSize(BRICK_WIDTH, BRICK_HEIGHT)
            )
        }

    }
}