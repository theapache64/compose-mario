package com.theapache64.composemario

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.core.MarioGame
import com.theapache64.composemario.core.R
import com.theapache64.composemario.core.base.Game
import com.theapache64.composemario.theme.CornflowerBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.awt.event.KeyEvent

const val WINDOW_WIDTH = 1020
const val WINDOW_HEIGHT = 600


val focusRequester = FocusRequester()

val marioPaint by lazy {
    Paint().apply { filterQuality = FilterQuality.None }
}


fun main() {

    val game: Game = MarioGame()

    Window(
        title = "Compose Mario",
        size = IntSize(WINDOW_WIDTH, WINDOW_HEIGHT),
        resizable = false
    ) {


        LaunchedEffect(Unit) {
            delay(1000)
            focusRequester.requestFocus()

            while (isActive) {
                delay(60)
                game.step()
            }

        }


        val gameFrame = game.gameFrame.value

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusModifier()
                .onPreviewKeyEvent {
                    println(it.nativeKeyEvent.id)
                    when (it.nativeKeyEvent.id) {
                        KeyEvent.KEY_PRESSED -> {
                            when (it.key) {
                                Key.DirectionRight -> {
                                    println("Moving right")
                                    game.setDirection(Direction.MOVE_RIGHT).let { true }
                                }

                                Key.DirectionLeft -> {
                                    game.setDirection(Direction.MOVE_LEFT).let { true }
                                }
                                else -> false
                            }
                        }

                        KeyEvent.KEY_RELEASED -> {
                            when (it.key) {
                                Key.DirectionRight -> {
                                    game.setDirection(Direction.IDLE_RIGHT).let { true }
                                }

                                Key.DirectionLeft -> {
                                    game.setDirection(Direction.IDLE_LEFT).let { true }
                                }

                                else -> false
                            }
                        }

                        else -> {
                            false
                        }
                    }

                }
        ) {

            // Rendering the frame
            println("Rendering new frame...")

            // Sky
            drawRect(
                color = CornflowerBlue
            )

            // Floor
            val floorBricks = gameFrame.floorBricks

            val visibleBricks = floorBricks.filter {
                it.x in -MarioGame.BRICK_WIDTH..WINDOW_WIDTH && it.y in 0..WINDOW_HEIGHT
            } // optimising

            for (floorBrick in visibleBricks) {

                // Floor
                drawImage(
                    image = R.graphics.brickPng,
                    dstOffset = IntOffset(floorBrick.x, floorBrick.y),
                    dstSize = IntSize(MarioGame.BRICK_WIDTH, MarioGame.BRICK_HEIGHT)
                )
            }

            // Mario
            val mario = gameFrame.mario
            drawIntoCanvas { canvas ->
                canvas.drawImageRect(
                    image = R.graphics.marioSprite,
                    paint = marioPaint,
                    srcOffset = mario.action.srcOffset,
                    srcSize = mario.action.srcSize,
                    dstOffset = mario.dstOffset,
                    dstSize = mario.action.dstSize,
                )
            }
        }
    }

}