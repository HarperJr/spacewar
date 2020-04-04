package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.Color
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.entity.impl.EntityEnemy
import com.harper.spacewar.main.entity.impl.EntityPlayer
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.shader.definition.SpriteShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.SpriteShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.resource.MeshesResources
import com.harper.spacewar.main.resource.RegistryRes
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager
import com.harper.spacewar.utils.lookRotation
import org.joml.Matrix4f
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

class SpaceshipRenderer(renderManager: RenderManager) :
    ModelBasedEntityRenderer<Entity>(renderManager) {
    override val meshesResources: MeshesResources =
        renderManager.resourceRegistry.getModelResource(RegistryRes.SPACESHIP)
    private val fontRenderer: FontRenderer = renderManager.fontRenderer

    private val spriteTexture: Texture = renderManager.textureManager.provideTexture("gui/game.png")
    private val spriteShader: SpriteShader = ShaderProvider.provide(SpriteShaderDefinition)
    private val decimalFormat: DecimalFormat = DecimalFormat("#.## %")

    private val modelMatrix: Matrix4f = Matrix4f()

    override fun renderEntityShape(entity: Entity, camera: Camera, x: Float, y: Float, z: Float) {
        GlUtils.glEnable(GlUtils.DEPTH_TEST)
        GlUtils.glDepthFunc(GlUtils.DEPTH_LESS)
        super.renderEntityShape(entity, camera, x, y, z)

        // Render health sprite for leaving spaceships
        if (entity is EntityLiving && entity !is EntityPlayer) {
            spriteShader.use<SpriteShader> {
                GlUtils.glEnable(GlUtils.TEXTURE_2D)
                GlUtils.glEnable(GlUtils.DEPTH_TEST)
                GlUtils.glEnable(GlUtils.BLEND)
                GlUtils.glDepthFunc(GlUtils.DEPTH_ALWAYS)
                GlUtils.glBlendFuncDefault()

                GlUtils.glActiveTexture(GlUtils.TEXTURE_0)
                GlUtils.glBindTexture(spriteTexture.glTexture)
                bindTexture(0)

                val spriteScale = 0.05f
                val spriteWidth = spriteTexture.width / 2f
                val spriteHeight = spriteTexture.height / 8f

                val barColor = if (entity is EntityEnemy) Color.RED else Color.GREEN

                val bounds = entity.getBounds()
                val entityHeight = bounds.maxY - bounds.minY
                bindMatrices(getModelMatrixForSprite(camera, x, y - entityHeight - spriteHeight * spriteScale, z), camera.view, camera.projection)

                renderHealthBarBackground(spriteWidth, spriteHeight, spriteScale)
                renderHealthBarForeground(entity, spriteWidth, spriteHeight, spriteScale, barColor)
                fontRenderer.drawCenteredText(decimalFormat.format(entity.healthPercent), 0f, 0f, 0xffffffff, spriteScale)

                GlUtils.glDisable(GlUtils.BLEND)
                GlUtils.glDisable(GlUtils.DEPTH_TEST)
                GlUtils.glDisable(GlUtils.TEXTURE_2D)
            }
        }

        GlUtils.glDisable(GlUtils.DEPTH_TEST)
    }

    private fun renderHealthBarBackground(spriteWidth: Float, spriteHeight: Float, spriteScale: Float) {
        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            val spriteYa = spriteHeight * 2f
            val spriteXa = 0f

            pos(-spriteWidth * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXa, (spriteYa + spriteHeight) / spriteTexture.height).completeVertex()

            pos(0f, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / spriteTexture.width, (spriteYa + spriteHeight) / spriteTexture.height).completeVertex()

            pos(0f, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / spriteTexture.width, spriteYa / spriteTexture.height).completeVertex()

            pos(-spriteWidth * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXa, spriteYa / spriteTexture.height).completeVertex()

            val spriteYb = spriteHeight * 3f
            val spriteXb = 0f
            pos(0f, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXb, (spriteYb + spriteHeight) / spriteTexture.height).completeVertex()

            pos(spriteWidth * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / spriteTexture.width, (spriteYb + spriteHeight) / spriteTexture.height).completeVertex()

            pos(spriteWidth * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / spriteTexture.width, spriteYb / spriteTexture.height).completeVertex()

            pos(0f, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXb, spriteYb / spriteTexture.height).completeVertex()
        }
    }

    private fun renderHealthBarForeground(entity: EntityLiving, spriteWidth: Float, spriteHeight: Float, spriteScale: Float, color: Long) {
        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX_COLOR) {
            val spriteYa = spriteHeight * 0f
            val spriteXa = 0f

            val adjustedSpriteWidthA = min(1f, entity.healthPercent * 2f) * spriteWidth
            pos(-spriteWidth * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXa, (spriteYa + spriteHeight) / spriteTexture.height)
                .color(color).completeVertex()

            pos((adjustedSpriteWidthA - spriteWidth) * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(adjustedSpriteWidthA / spriteTexture.width, (spriteYa + spriteHeight) / spriteTexture.height)
                .color(color).completeVertex()

            pos((adjustedSpriteWidthA - spriteWidth) * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(adjustedSpriteWidthA / spriteTexture.width, spriteYa / spriteTexture.height)
                .color(color).completeVertex()

            pos(-spriteWidth * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXa, spriteYa / spriteTexture.height)
                .color(color).completeVertex()

            val spriteYb = spriteHeight * 1f
            val spriteXb = 0f

            val adjustedSpriteWidthB = max(0f, (entity.healthPercent - 0.5f) * 2f) * spriteWidth
            pos(0f, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXb, (spriteYb + spriteHeight) / spriteTexture.height)
                .color(color).completeVertex()

            pos(adjustedSpriteWidthB * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(adjustedSpriteWidthB / spriteTexture.width, (spriteYb + spriteHeight) / spriteTexture.height)
                .color(color).completeVertex()

            pos(adjustedSpriteWidthB * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(adjustedSpriteWidthB / spriteTexture.width, spriteYb / spriteTexture.height)
                .color(color).completeVertex()

            pos(0f, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteXb, spriteYb / spriteTexture.height)
                .color(color).completeVertex()
        }
    }

    private fun getModelMatrixForSprite(camera: Camera, x: Float, y: Float, z: Float): Matrix4f {
        modelMatrix.identity()
        return modelMatrix.apply {
            translate(x, y, z)
            rotate(lookRotation(camera.lookVec, camera.upVector))
        }
    }
}
