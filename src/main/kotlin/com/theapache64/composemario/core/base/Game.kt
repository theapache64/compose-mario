package com.theapache64.composemario.core.base

import androidx.compose.runtime.State
import com.theapache64.composemario.core.Direction
import com.theapache64.composemario.models.GameFrame

interface Game {
    val gameFrame: State<GameFrame>
    fun step()
    fun setDirection(direction: Direction)
}