package com.harper.spacewar.main.entity

import com.harper.spacewar.main.damage.DamageSource
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.lookRotation
import org.joml.Vector3f

abstract class EntityLiving(scene: Scene, private val maxHealth: Float) : Entity(scene) {
    abstract val rotationSpeed: Float

    var isDead = false
        protected set

    val lookAt: Vector3f = Vector3f(0f, 0f, 1f)
    private val prevLookAt: Vector3f = Vector3f()

    // This value is always normalized [0f; 1f]
    var health: Float = maxHealth
        get() = field / this.maxHealth

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
        this.isDead = false
    }

    override fun update(time: Float) {
        if (this.health <= 0f)
            this.isDead = true

        super.update(time)
    }

    fun receiveDamageFromEntity(entity: Entity, source: DamageSource) {
        source.onDamage(this)
    }

    fun lookAt(time: Float, lookVec: Vector3f) {
        this.lookAt.set(
            this.lookAt.x + ((lookVec.x - this.prevLookAt.x) * time) * rotationSpeed,
            this.lookAt.y + ((lookVec.y - this.prevLookAt.y) * time) * rotationSpeed,
            this.lookAt.z + ((lookVec.z - this.prevLookAt.z) * time) * rotationSpeed
        ).normalize()

        this.prevLookAt.set(this.lookAt)
        this.rotation.set(lookRotation(this.lookAt, this.camera.upVector))
    }
}