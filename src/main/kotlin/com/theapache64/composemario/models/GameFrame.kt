package com.theapache64.composemario.models

import com.theapache64.composemario.core.Direction

data class GameFrame(
    val mario: Mario,
    val floorBricks: List<FloorBrick>,
    val clouds: List<Cloud>,
    val goombas: List<Goomba>,
    val direction: Direction,
    val isGameOver: Boolean = false,
)