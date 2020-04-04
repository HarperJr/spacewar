package com.harper.spacewar.main.entity.sprite

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.lookRotation
import org.joml.Rectanglef
import org.joml.Vector3f

class EntitySprite(scene: Scene, val rect: Rectanglef) : Entity(scene) {
    private val lookAt: Vector3f = Vector3f()

    override fun update(time: Float) {
        this.lookAt.set(
            this.camera.lookVec.x - this.position.x,
            this.camera.lookVec.y - this.position.y,
            this.camera.lookVec.z - this.position.z
        )

        this.rotation.set(lookRotation(this.lookAt, camera.upVector))
    }
}