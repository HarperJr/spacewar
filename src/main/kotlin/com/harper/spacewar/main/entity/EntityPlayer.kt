package com.harper.spacewar.main.entity

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.scene.SceneInGame
import org.joml.Vector3f
import kotlin.math.asin

class EntityPlayer(private val sceneInGame: SceneInGame) : EntitySpaceship(sceneInGame) {
    private val logger: Logger = Logger.getLogger<EntityPlayer>()

    val lookAt: Vector3f = Vector3f(0f, 0f, 1f)
    var accelerate: Boolean = false

    private var prevLookAt: Vector3f = Vector3f()

    private val camera: Camera
        get() = sceneInGame.camera

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
    }

    override fun update(time: Float) {
        if (!this.sceneInGame.isPaused) {
            val camLookVec = this.camera.lookVec

            this.lookAt.set(
                this.lookAt.x + ((camLookVec.x - this.prevLookAt.x) * time) * ROTATION_SPEED,
                this.lookAt.y + ((camLookVec.y - this.prevLookAt.y) * time) * ROTATION_SPEED,
                this.lookAt.z + ((camLookVec.z - this.prevLookAt.z) * time) * ROTATION_SPEED
            )

            this.prevLookAt.set(this.lookAt)

            val yaw = asin(this.lookAt.x) / Math.PI * 180f
            val pitch = -asin(this.lookAt.y) / Math.PI * 180f

            //logger.debug("Entity Player rotation yaw: $yaw, pitch: $pitch")

            if (accelerate) {
                move(this.lookAt.x * SPEED, this.lookAt.y * SPEED, this.lookAt.z * SPEED)
            } else {
                move(this.lookAt.x * SPEED, this.lookAt.y * SPEED, this.lookAt.z * SPEED)
            }


            super.rotate(yaw.toFloat(), pitch.toFloat())
        }

        super.update(time)
    }

    fun onMouseClicked() {

    }

    companion object {
        private const val ROTATION_SPEED = 0.035f
        private const val ACCELERATION_SPEED = 1f
        private const val SPEED = 0.5f
    }
}