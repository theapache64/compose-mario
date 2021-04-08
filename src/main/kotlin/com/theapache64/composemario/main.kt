package com.theapache64.composemario

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.theme.CornflowerBlue
import com.theapache64.composemario.theme.OrangeRoughy
import kotlinx.coroutines.delay
import java.awt.event.KeyEvent
import kotlin.math.ceil

private const val WINDOW_WIDTH = 1020
private const val WINDOW_HEIGHT = 600
private const val LEVEL_LENGTH = WINDOW_WIDTH * 2 // 10 times the screen

// Brick
private const val BRICK_WIDTH = 30
private const val BRICK_HEIGHT = 30
private val FLOOR_BRICK_COLOR = OrangeRoughy

const val minY = (WINDOW_HEIGHT * 0.75).toInt()

fun getFloorBricks(): List<FloorBrick> {
    val floorBricks = mutableListOf<FloorBrick>()
    val brickColumns = ceil(LEVEL_LENGTH / BRICK_WIDTH.toFloat()).toInt()
    val brickRows = (WINDOW_HEIGHT - minY) / BRICK_HEIGHT
    repeat(brickRows) { rowIndex ->
        repeat(brickColumns) { columnIndex ->
            val bx = (BRICK_WIDTH * columnIndex)
            val by = minY + (BRICK_HEIGHT * rowIndex)
            print("[$bx, $by] ")
            val floorBrick = FloorBrick(bx, by)
            floorBricks.add(floorBrick)
        }
        println(" ")
    }

    return floorBricks
}

val focusRequester = FocusRequester()

val brickPng = imageFromResource("floor.png")
val marioSprite = imageFromResource("mario_sprite.png")

fun main() = Window(
    title = "Compose Mario",
    size = IntSize(WINDOW_WIDTH, WINDOW_HEIGHT),
    resizable = false
) {

    val floorBricks = remember { mutableStateListOf(*getFloorBricks().toTypedArray()) }

    LaunchedEffect(Unit) {
        delay(1000)
        focusRequester.requestFocus()
    }


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusModifier()
            .onPreviewKeyEvent {
                println(it)
                if (it.nativeKeyEvent.id == KeyEvent.KEY_PRESSED) {
                    when (it.key) {
                        Key.DirectionRight -> {
                            val newBrickList = floorBricks.map { brick ->
                                brick.x = brick.x - 10
                                brick
                            }

                            floorBricks.clear()
                            floorBricks.addAll(newBrickList)

                            true
                        }

                        Key.DirectionLeft -> {
                            val newBrickList = floorBricks.map { brick ->
                                brick.x = brick.x + 10
                                brick
                            }

                            floorBricks.clear()
                            floorBricks.addAll(newBrickList)

                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }

            }
    ) {

        // Sky
        drawRect(
            color = CornflowerBlue
        )

        // Floor
        val visibleBricks = floorBricks.filter {
            it.x in -BRICK_WIDTH..WINDOW_WIDTH && it.y in 0..WINDOW_HEIGHT
        } // optimising

        println("${floorBricks.size - visibleBricks.size} bricks optimized")

        for (floorBrick in visibleBricks) {

            // Floor
            drawImage(
                image = brickPng,
                dstOffset = IntOffset(floorBrick.x, floorBrick.y),
                dstSize = IntSize(BRICK_WIDTH, BRICK_HEIGHT)
            )
        }


        drawImage(
            image = marioSprite,
            srcOffset = IntOffset(209, 52),
            srcSize = IntSize(16, 32),

            dstOffset = IntOffset(20, minY - 60),
            dstSize = IntSize(30, 60),
        )
    }
}