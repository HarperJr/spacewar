package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.scene.renderer.RenderManager

class PlayerEntity(private val sceneInGame: SceneInGame, renderManager: RenderManager) :
    SpaceshipEntity(renderManager) {

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
    }

    override fun update(time: Float) {

        super.update(time)
    }
}