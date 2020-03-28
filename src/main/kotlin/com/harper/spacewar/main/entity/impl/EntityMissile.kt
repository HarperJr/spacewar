package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.damage.DamageSource
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.sprite.Sprite
import org.joml.AABBf
import org.joml.Vector3f

class EntityMissile(private val sceneInGame: SceneInGame, private val producerEntity: EntityLiving) :
    EntityLiving(sceneInGame, 1f) {
    override val rotationSpeed: Float = 10f

    override val sprites: List<Sprite> = emptyList()

    private var velocityVec: Vector3f = Vector3f(producerEntity.lookAt)

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 2f, 2f, 5f)
    }

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
    }

    override fun update(time: Float) {
        if (!this.sceneInGame.isPaused) {
            super.update(time)

            super.lookAt(time, this.velocityVec)
            super.move(
                velocityVec.x * SPEED,
                velocityVec.y * SPEED,
                velocityVec.z * SPEED
            )

            val entities = this.sceneInGame.getEntitiesCollidedExcept(this, listOf(this.producerEntity, this))
            if (entities.isNotEmpty()) {
                val entityHit = entities.first()
                if (entityHit is EntityLiving && entityHit != producerEntity) {
                    entityHit.receiveDamageFromEntity(this, DamageSource.MISSILE)
                    this.isDead = true
                }
            }
        }
    }

    companion object {
        private const val SPEED = 100f
    }
}
