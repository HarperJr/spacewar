package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.scene.SceneInGame

class EntityPlayer(sceneInGame: SceneInGame) : EntitySpaceship(sceneInGame, 100f) {
    override val movementSpeed: Float = 0.2f
    override val rotationSpeed: Float = ROTATION_SPEED

    var accelerate: Boolean = false

    override fun update(time: Float) {
        super.update(time)
        super.lookAt(time, super.camera.lookVec)
    }

    fun onMousePressed() {
        makeShot()
    }

    companion object {
        private const val ROTATION_SPEED = 0.125f
    }
}