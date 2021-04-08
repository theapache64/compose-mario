package com.theapache64.composemario.models

import com.theapache64.composemario.core.Direction

data class GameFrame(
    val mario: Mario,
    val floorBricks: List<FloorBrick>,
    val goombas: List<Goomba>,
    val direction: Direction,
)