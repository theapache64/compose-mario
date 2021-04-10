package com.theapache64.composemario

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.theme.ComposeMarioTheme
import com.theapache64.composemario.theme.CornflowerBlue
import kotlinx.coroutines.delay
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

        ComposeMarioTheme {

            val gameFrame = game.gameFrame.value

            LaunchedEffect(gameFrame.isGameOver) {
                delay(1000)
                focusRequester.requestFocus()

                while (!gameFrame.isGameOver) {
                    delay(60)
                    game.step()
                }

            }

            Box(
                contentAlignment = Alignment.Center
            ) {

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

                    // Rendering floor bricks
                    for (floorBrick in gameFrame.floorBricks.filterVisibleBricks()) {
                        // FloorBrick
                        drawImage(
                            image = R.graphics.brickPng,
                            dstOffset = IntOffset(floorBrick.x, floorBrick.y),
                            dstSize = IntSize(FloorBrick.BRICK_WIDTH, FloorBrick.BRICK_HEIGHT),
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

                    // TODO: To remove : only for debug
                    val marioFootX = mario.dstOffset.x
                    val marioFootY = mario.dstOffset.y + mario.action.dstSize.height
                    drawCircle(
                        Color.Red,
                        3f,
                        center = Offset(marioFootX.toFloat(), marioFootY.toFloat())
                    )
                }

                if (gameFrame.isGameOver) {
                    Text(
                        text = "GAME OVER",
                        style = MaterialTheme.typography.h1
                    )
                }
            }


        }

    }

}

private fun List<FloorBrick>.filterVisibleBricks(): List<FloorBrick> {
    return this.filter { brick ->
        brick.x in -FloorBrick.BRICK_WIDTH..WINDOW_WIDTH && brick.y in 0..WINDOW_HEIGHT
    }
}
