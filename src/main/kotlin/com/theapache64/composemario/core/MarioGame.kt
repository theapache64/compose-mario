package com.theapache64.composemario.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.theapache64.composemario.WINDOW_HEIGHT
import com.theapache64.composemario.WINDOW_WIDTH
import com.theapache64.composemario.core.base.Game
import com.theapache64.composemario.models.*
import com.theapache64.composemario.models.Cloud.Companion.stepClouds
import com.theapache64.composemario.models.FloorBrick.Companion.stepFloorBricks
import com.theapache64.composemario.models.Forest.Companion.stepForests
import com.theapache64.composemario.models.Mario.Companion.stepMario
import com.theapache64.composemario.models.Mountain.Companion.stepMountains
import com.theapache64.composemario.models.Tube.Companion.stepTubes

class MarioGame : Game {

    companion object {

        const val MARIO_SPEED = 15
        const val CLOUD_SPEED = (MARIO_SPEED * 0.3f).toInt()
        const val MOUNTAIN_SPEED = (MARIO_SPEED * 0.5f).toInt()
        const val FOREST_SPEED = (MARIO_SPEED * 0.8f).toInt()
        const val TUBE_SPEED = MARIO_SPEED
        const val LEVEL_LENGTH = WINDOW_WIDTH * 5 // x times the screen

        /**
         * 30% bricks so 70% play area. this may become dynamic as we build more levels
         */
        const val BRICK_START_Y = (WINDOW_HEIGHT * 0.70).toInt()
    }


    private val _gameFrame: MutableState<GameFrame> by lazy {

        mutableStateOf(
            // First frame
            GameFrame(
                mario = Mario(action = Mario.START_ACTION, dstOffset = Mario.START_OFFSET),
                floorBricks = FloorBrick.createFloorBricks(), // building floor bricks
                mountains = Mountain.createMountains(),
                clouds = Cloud.createClouds(),
                forests = Forest.createForests(),
                tubes = Tube.createTubes(),
                goombas = listOf(), // TODO : I can't wait to implement this xD
                directions = mutableSetOf(Direction.IDLE_RIGHT), // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        update {
            val newBricks = floorBricks.stepFloorBricks(mario, directions)
            val newMario = mario.stepMario(directions, newBricks)
            val isGameOver = newMario.dstOffset.y > WINDOW_HEIGHT


            val newClouds = clouds.stepClouds(directions, newMario)
            val newMountains = mountains.stepMountains(directions, newMario)
            val newForests = forests.stepForests(directions, newMario)
            val newTubes = tubes.stepTubes(directions, newMario)

            copy(
                mario = newMario,
                floorBricks = newBricks,
                isGameOver = isGameOver,
                clouds = newClouds,
                mountains = newMountains,
                forests = newForests,
                tubes = newTubes
            )
        }
    }


    override fun setDirection(newDirection: Direction) {
        update {
            val newDirections = directions.apply {
                if (newDirection != Direction.MOVE_UP) {
                    clear()
                }

                add(newDirection)
            }

            println(newDirections)

            copy(
                directions = newDirections
            )
        }
    }

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}


