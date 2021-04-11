@file:Suppress("ClassName")

package com.theapache64.composemario.core

import androidx.compose.ui.graphics.imageFromResource

object R {

    object graphics {
        val brickPng by lazy {
            imageFromResource("graphics/floor.png")
        }
        val marioSprite by lazy {
            imageFromResource("graphics/mario_action_sprite.png")
        }

        val scenerySprite by lazy {
            imageFromResource("graphics/scenery_sprite.png")
        }
    }
}