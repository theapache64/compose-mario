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
import com.theapache64.composemario.models.Cloud.Companion.filterVisibleClouds
import com.theapache64.composemario.models.FloorBrick
import com.theapache64.composemario.models.FloorBrick.Companion.filterVisibleBricks
import com.theapache64.composemario.models.Forest.Companion.filterVisibleForests
import com.theapache64.composemario.models.Mountain.Companion.filterVisibleMountains
import com.theapache64.composemario.models.Tube.Companion.filterVisibleTubes
import com.theapache64.composemario.theme.ComposeMarioTheme
import com.theapache64.composemario.theme.CornflowerBlue
import kotlinx.coroutines.delay
import java.awt.event.KeyEvent

const val WINDOW_WIDTH = 1020
const val WINDOW_HEIGHT = 600


val focusRequester = FocusRequester()

val pixelPaint by lazy {
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


                    // Mario
                    val mario = gameFrame.mario
                    drawIntoCanvas { canvas ->

                        // Rendering floor bricks
                        for (floorBrick in gameFrame.floorBricks.filterVisibleBricks()) {
                            // FloorBrick
                            canvas.drawImageRect(
                                image = R.graphics.brickPng,
                                dstOffset = IntOffset(floorBrick.x, floorBrick.y),
                                dstSize = IntSize(FloorBrick.BRICK_WIDTH, FloorBrick.BRICK_HEIGHT),
                                paint = pixelPaint
                            )
                        }


                        // Draw cloud
                        for (cloud in gameFrame.clouds.filterVisibleClouds()) {
                            canvas.drawImageRect(
                                image = R.graphics.scenerySprite,
                                paint = pixelPaint,
                                srcOffset = cloud.type.srcOffset,
                                srcSize = cloud.type.srcSize,
                                dstOffset = IntOffset(cloud.x, cloud.y),
                                dstSize = cloud.type.dstSize,
                            )
                        }

                        // Draw mountains
                        for (mountain in gameFrame.mountains.filterVisibleMountains()) {
                            canvas.drawImageRect(
                                image = R.graphics.scenerySprite,
                                paint = pixelPaint,
                                srcOffset = mountain.type.srcOffset,
                                srcSize = mountain.type.srcSize,
                                dstOffset = IntOffset(mountain.x, mountain.y),
                                dstSize = mountain.type.dstSize,
                            )
                        }

                        // Draw forests
                        for (forest in gameFrame.forests.filterVisibleForests()) {
                            canvas.drawImageRect(
                                image = R.graphics.scenerySprite,
                                paint = pixelPaint,
                                srcOffset = forest.type.srcOffset,
                                srcSize = forest.type.srcSize,
                                dstOffset = IntOffset(forest.x, forest.y),
                                dstSize = forest.type.dstSize,
                            )
                        }

                        // Draw tubes
                        for (tube in gameFrame.tubes.filterVisibleTubes()) {
                            canvas.drawImageRect(
                                image = R.graphics.scenerySprite,
                                paint = pixelPaint,
                                srcOffset = tube.type.srcOffset,
                                srcSize = tube.type.srcSize,
                                dstOffset = IntOffset(tube.x, tube.y),
                                dstSize = tube.type.dstSize,
                            )
                        }


                        // Draw mario
                        canvas.drawImageRect(
                            image = R.graphics.marioSprite,
                            paint = pixelPaint,
                            srcOffset = mario.action.srcOffset,
                            srcSize = mario.action.srcSize,
                            dstOffset = mario.dstOffset,
                            dstSize = mario.action.dstSize,
                        )
                    }

                    // TODO: To remove : only for debug
                    /*val marioFootX = mario.dstOffset.x
                    val marioFootY = mario.dstOffset.y + mario.action.dstSize.height
                    drawCircle(
                        Color.Red,
                        3f,
                        center = Offset(marioFootX.toFloat(), marioFootY.toFloat())
                    )*/
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


