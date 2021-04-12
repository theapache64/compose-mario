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
        const val CLOUD_SPEED = 5
        const val MOUNTAIN_SPEED = 7
        const val FOREST_SPEED = 6
        const val TUBE_SPEED = MARIO_SPEED
        const val LEVEL_LENGTH = WINDOW_WIDTH * 5 // x times the screen

        /**
         * 30% bricks so 70% play area. this may become dynamic as we build more levels
         */
        const val BRICK_START_Y = (WINDOW_HEIGHT * 0.70).toInt()
        const val MAX_JUMP_HEIGHT = (WINDOW_HEIGHT * 0.30)
        const val JUMP_SPEED = FloorBrick.BRICK_HEIGHT
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
                goombas = listOf(), // TODO : I can't wait implement this xD
                direction = Direction.IDLE_RIGHT, // Face right
            )
        )
    }


    override val gameFrame: State<GameFrame> = _gameFrame

    override fun step() {
        update {
            val newBricks = floorBricks.stepFloorBricks(mario, direction)
            val newMario = mario.stepMario(direction, newBricks)
            val isGameOver = newMario.dstOffset.y > WINDOW_HEIGHT

            val newDirection = if (direction == Direction.UP && newMario.dstOffset.y <= MAX_JUMP_HEIGHT) {
                Direction.DOWN
            } else if (direction == Direction.DOWN && newMario.dstOffset.y >= (BRICK_START_Y - FloorBrick.BRICK_HEIGHT)) {
                Direction.IDLE_RIGHT
            } else {
                direction
            }

            val newClouds = clouds.stepClouds(direction)
            val newMountains = mountains.stepMountains(direction)
            val newForests = forests.stepForests(direction)
            val newTubes = tubes.stepTubes(direction)

            copy(
                mario = newMario,
                floorBricks = newBricks,
                isGameOver = isGameOver,
                direction = newDirection,
                clouds = newClouds,
                mountains = newMountains,
                forests = newForests,
                tubes = newTubes
            )
        }
    }

    override fun setDirection(direction: Direction) {
        update {
            copy(
                direction = direction
            )
        }
    }

    private inline fun update(func: GameFrame.() -> GameFrame) {
        _gameFrame.value = _gameFrame.value.func()
    }
}


