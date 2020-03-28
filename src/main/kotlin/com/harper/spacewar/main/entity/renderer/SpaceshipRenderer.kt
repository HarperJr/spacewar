package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
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

class SpaceshipRenderer(renderManager: RenderManager) :
    ModelBasedEntityRenderer<Entity>(renderManager) {
    override val meshesResources: MeshesResources =
        renderManager.resourceRegistry.getModelResource(RegistryRes.SPACESHIP)

    private val texture: Texture = renderManager.textureManager.provideTexture("gui/game.png")
    private val spriteShader: SpriteShader = ShaderProvider.provide(SpriteShaderDefinition)

    private val modelMatrix: Matrix4f = Matrix4f()

    override fun renderEntityShape(entity: Entity, camera: Camera, x: Float, y: Float, z: Float) {
        super.renderEntityShape(entity, camera, x, y, z)

        // Render health sprite for leaving spaceships
        if (entity is EntityLiving) {
            spriteShader.use<SpriteShader> {
                GlUtils.glEnable(GlUtils.TEXTURE_2D)
                GlUtils.glBlendFuncDefault()


                GlUtils.glActiveTexture(GlUtils.TEXTURE_0)
                GlUtils.glBindTexture(texture.glTexture)
                bindTexture(0)

                val spriteScale = 0.25f
                val spriteWidth = texture.width / 2f
                val spriteHeight = texture.height / 8f

                bindMatrices(getModelMatrixForSprite(camera, x, y - spriteHeight, z), camera.view, camera.projection)
                renderBackgroundBar(spriteWidth, spriteHeight, spriteScale)

                GlUtils.glDisable(GlUtils.TEXTURE_2D)
            }
        }
    }

    private fun renderBackgroundBar(spriteScale: Float, spriteWidth: Float, spriteHeight: Float) {
        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            pos(-spriteWidth / 2f * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(0f, (spriteHeight * 4f) / texture.height).completeVertex()

            pos(spriteWidth / 2f * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / texture.width, (spriteHeight * 4f) / texture.height).completeVertex()

            pos(spriteWidth / 2f * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / texture.width, 0f).completeVertex()

            pos(-spriteWidth / 2f * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(32f, 32f).completeVertex()



            pos(-spriteWidth / 2f * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(0f, spriteHeight / texture.height).completeVertex()

            pos(spriteWidth / 2f * spriteScale, spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / texture.width, spriteHeight / texture.height).completeVertex()

            pos(spriteWidth / 2f * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(spriteWidth / texture.width, 0f).completeVertex()

            pos(-spriteWidth / 2f * spriteScale, -spriteHeight / 2f * spriteScale, 0f)
                .tex(0f, 0f).completeVertex()
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
