package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.sprite.Sprite
import org.joml.AABBf
import org.joml.Vector3f

class EntityPlayer(private val sceneInGame: SceneInGame) : EntityLiving(sceneInGame, 1f) {
    override val rotationSpeed: Float = ROTATION_SPEED
    override val sprites: List<Sprite> = emptyList()

    var accelerate: Boolean = false

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 100f, 20f, 100f)
    }

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
    }

    override fun update(time: Float) {
        if (!this.sceneInGame.isPaused) {
            super.lookAt(time, super.camera.lookVec)

            if (accelerate) {
                move(this.lookAt.x * SPEED, this.lookAt.y * SPEED, this.lookAt.z * SPEED)
            } else move(this.lookAt.x * SPEED, this.lookAt.y * SPEED, this.lookAt.z * SPEED)
        }

        super.update(time)
    }

    fun onMousePressed() {
        if (this.sceneInGame.isPaused) return

        val playerWidth = (this.axisAlignedBox.maxX - this.axisAlignedBox.minX) * 0.8f
        val missileDistance = playerWidth / (MAX_MISSILES_PER_SHOT - 1)
        val rightVec = this.lookAt.cross(this.camera.upVector, Vector3f()).normalize()
        val missilesStartPos = rightVec.mul(playerWidth / 2f, Vector3f())
        for (i in 0 until MAX_MISSILES_PER_SHOT)
            this.sceneInGame.addEntity(
                EntityMissile(this.sceneInGame, this),
                center.x + missilesStartPos.x - rightVec.x * missileDistance * i,
                center.y,
                center.z + missilesStartPos.z - rightVec.z * missileDistance * i
            )
    }

    companion object {
        private const val ROTATION_SPEED = 0.125f
        private const val ACCELERATION_SPEED = 1f
        private const val SPEED = 2f
        private const val MAX_MISSILES_PER_SHOT = 2
    }
}