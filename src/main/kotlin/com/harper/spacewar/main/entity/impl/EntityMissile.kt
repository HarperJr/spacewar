package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.damage.DamageSource
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.scene.Scene
import org.joml.AABBf
import org.joml.Vector3f

class EntityMissile(private val sceneInGame: Scene, val producerEntity: EntityLiving) :
    EntityLiving(sceneInGame, 1f) {
    override val movementSpeed: Float = 10f
    override val rotationSpeed: Float = 10f

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var createdTimeMillis: Long = currentTimeMillis

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 0.4f, 0.4f, 0.4f)
    }

    override fun create(id: Int, x: Float, y: Float, z: Float) {
        super.create(id, x, y, z)
        this.createdTimeMillis = this.currentTimeMillis

        val rightVec = this.position.sub(this.producerEntity.position, Vector3f())
        super.lookAt.set(
            producerEntity.lookAt.x * MAX_LIFE_TIME_MILLIS / 2f - rightVec.x,
            producerEntity.lookAt.y * MAX_LIFE_TIME_MILLIS / 2f - rightVec.y,
            producerEntity.lookAt.z * MAX_LIFE_TIME_MILLIS / 2f - rightVec.z
        )
    }

    override fun update(time: Float) {
        super.update(time)
        super.lookAt(time, this.lookAt)

        val collidedEntities = this.sceneInGame.getEntitiesCollidedExcept(this, listOf(this.producerEntity, this))
        if (collidedEntities.isNotEmpty()) {
            val entityHit = collidedEntities.first()
            if (entityHit is EntityLiving) {
                entityHit.receiveDamageFromEntity(this, DamageSource.MISSILE)
                this.isDead = true
            }
        }

        if (this.createdTimeMillis + MAX_LIFE_TIME_MILLIS <= this.currentTimeMillis)
            this.isDead = true
    }

    companion object {
        private const val MAX_LIFE_TIME_MILLIS = 5000L
    }
}