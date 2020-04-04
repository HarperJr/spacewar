package com.harper.spacewar.main.scene.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.impl.*
import com.harper.spacewar.main.entity.renderer.EntityRenderer
import com.harper.spacewar.main.entity.renderer.MissileRenderer
import com.harper.spacewar.main.entity.renderer.SpaceshipRenderer
import com.harper.spacewar.main.entity.sprite.Sprite
import com.harper.spacewar.main.entity.sprite.renderer.SpriteRenderer
import com.harper.spacewar.main.entity.sprite.renderer.TileSpriteRenderer
import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.resource.ResourceRegistry
import com.harper.spacewar.main.scene.Camera
import kotlin.reflect.KClass

class RenderManager(
    val textureManager: TextureManager,
    val resourceRegistry: ResourceRegistry,
    val fontRenderer: FontRenderer
) {
    private lateinit var entityRenderers: Map<KClass<*>, EntityRenderer<*>>
    private lateinit var spriteRenderers: Map<KClass<*>, SpriteRenderer<*>>
    private lateinit var guiRenderer: GuiRenderer

    fun renderEntity(entity: Entity, camera: Camera) {
        (entityRenderers[entity::class] as? EntityRenderer<Entity>)?.render(
            entity,
            camera,
            entity.position.x,
            entity.position.y,
            entity.position.z
        )
    }

    fun renderSprite(sprite: Sprite, camera: Camera, x: Float, y: Float, z: Float) {
        (spriteRenderers[sprite::class] as? SpriteRenderer<Sprite>)?.render(sprite, camera, x, y, z)
    }

    fun initialize() {
        this.entityRenderers = mapOf(
            EntitySpaceshipStatic::class to SpaceshipRenderer(this),
            EntityPlayer::class to SpaceshipRenderer(this),
            EntityEnemy::class to SpaceshipRenderer(this),
            EntityMissile::class to MissileRenderer(this),
            EntityHeal::class to HealRenderer(this)
        )

        this.spriteRenderers = mapOf(
            Sprite::class to TileSpriteRenderer(this)
        )

        this.guiRenderer = GuiRenderer()
    }

    fun renderGui(scaledResolution: ScaledResolution, guiContainer: GuiContainer?) {
        if (guiContainer != null) {
            this.guiRenderer.render(scaledResolution, guiContainer)
        }
    }
}