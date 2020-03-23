package com.harper.spacewar.main.scene.renderer

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.EnemyEntity
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.PlayerEntity
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.entity.renderer.EntityRenderer
import com.harper.spacewar.main.entity.renderer.SpaceshipRenderer
import kotlin.reflect.KClass

class RenderManager(spacewar: Spacewar) {
    private val entityRenderers: Map<KClass<*>, EntityRenderer<*>> = mapOf(
        SpaceshipEntity::class to SpaceshipRenderer(spacewar),
        PlayerEntity::class to SpaceshipRenderer(spacewar),
        EnemyEntity::class to SpaceshipRenderer(spacewar)
    )

    fun renderEntity(entity: Entity, x: Float, y: Float, z: Float) {
        (entityRenderers[entity::class] as? EntityRenderer<Entity>)?.render(entity, x, y, z)
    }
}