package com.harper.spacewar.main.sprite

import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.lookRotation
import org.joml.Quaternionf
import org.joml.Vector3f

class Sprite(private val scene: Scene, val x: Float, val y: Float, val width: Float, val height: Float) {
    val position: Vector3f = Vector3f()
    val rotation: Quaternionf = Quaternionf()

    private val lookAt: Vector3f = Vector3f()

    private val camera: Camera
        get() = scene.camera

    fun update() {
        this.lookAt.set(
            this.camera.lookVec.x - this.position.x,
            this.camera.lookVec.y - this.position.y,
            this.camera.lookVec.z - this.position.z
        )

        this.rotation.set(lookRotation(this.lookAt, camera.upVector))
    }
}