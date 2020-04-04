package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.scene.Scene
import org.joml.AABBf
import org.joml.Vector3f

abstract class EntitySpaceship(private val scene: Scene, maxHealth: Float) : EntityLiving(scene, maxHealth) {
    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 8f, 2f, 8f)
    }

    fun makeShot() {
        super.isAttacking = true

        val entityWidth = (this.axisAlignedBox.maxX - this.axisAlignedBox.minX) * 0.75f
        val missileDistance = entityWidth / (MAX_MISSILES_PER_SHOT - 1)
        val rightVec = this.lookAt.cross(this.camera.upVector, Vector3f()).normalize()

        for (i in 0 until MAX_MISSILES_PER_SHOT) {
            this.scene.addEntity(
                EntityMissile(this.scene, this),
                center.x + (rightVec.x * entityWidth / 2f) - rightVec.x * missileDistance * i,
                center.y,
                center.z + (rightVec.z * entityWidth / 2f) - rightVec.z * missileDistance * i
            )
        }

        System.out.println("Pos center $center")
    }

    companion object {
        private const val MAX_MISSILES_PER_SHOT = 2
    }
}
