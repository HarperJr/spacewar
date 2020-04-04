package com.harper.spacewar.main.entity

import com.harper.spacewar.main.damage.DamageSource
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.lookRotation
import org.joml.Vector3f
import kotlin.math.max
import kotlin.math.min

abstract class EntityLiving(scene: Scene, private val maxHealth: Float) : Entity(scene) {
    abstract val movementSpeed: Float
    abstract val rotationSpeed: Float

    var isAttacking: Boolean = false

    var isDead = false
        protected set

    val lookAt: Vector3f = Vector3f(0f, 0f, 1f)

    private var health: Float = maxHealth

    var killedBy: EntityLiving? = null
        private set

    val healthPercent: Float
        get() = this.health / this.maxHealth

    private val prevLookAt: Vector3f = Vector3f()
    private val prevPosition: Vector3f = Vector3f(0f)

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var prevTimeMillis: Long = currentTimeMillis

    override fun create(id: Int, x: Float, y: Float, z: Float) {
        super.create(id, x, y, z)
        this.prevPosition.set(this.position)
        this.prevTimeMillis = this.currentTimeMillis
        this.isAttacking = false
        this.killedBy = null
        this.isDead = false
    }

    override fun update(time: Float) {
        if (this.health <= 0f)
            this.isDead = true

        if (this.prevTimeMillis + 1000L <= this.currentTimeMillis) {
            this.prevTimeMillis = this.currentTimeMillis
            this.isAttacking = false
        }

        this.lookAt.normalize()
        this.move(
            ((position.x + this.lookAt.x) - prevPosition.x) * time * movementSpeed,
            ((position.y + this.lookAt.y) - prevPosition.y) * time * movementSpeed,
            ((position.z + this.lookAt.z) - prevPosition.z) * time * movementSpeed
        )
        this.prevPosition.set(this.position)
    }

    fun addHealth(health: Float): Boolean {
        val prevHealth = this.health
        this.health = max(0f, min(this.maxHealth, prevHealth + health))
        return this.health != prevHealth
    }

    fun receiveDamageFromEntity(entity: EntityLiving, source: DamageSource) {
        source.onDamage(this)
        if (this.health <= 0f)
            killedBy = entity
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