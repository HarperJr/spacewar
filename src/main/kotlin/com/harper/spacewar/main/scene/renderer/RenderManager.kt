package com.harper.spacewar.main.scene.renderer

import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.EntityEnemy
import com.harper.spacewar.main.entity.EntityPlayer
import com.harper.spacewar.main.entity.EntitySpaceship
import com.harper.spacewar.main.entity.renderer.EntityRenderer
import com.harper.spacewar.main.entity.renderer.SpaceshipRenderer
import kotlin.reflect.KClass

class RenderManager(spacewar: Spacewar) {
    private val entityRenderers: Map<KClass<*>, EntityRenderer<*>> = mapOf(
        EntitySpaceship::class to SpaceshipRenderer(spacewar),
        EntityPlayer::class to SpaceshipRenderer(spacewar),
        EntityEnemy::class to SpaceshipRenderer(spacewar)
    )

    fun renderEntity(entity: Entity, camera: Camera, x: Float, y: Float, z: Float) {
        (entityRenderers[entity::class] as? EntityRenderer<Entity>)?.render(entity, camera, x, y, z)
    }
}