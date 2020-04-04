package com.harper.spacewar.main.scene.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.renderer.EntityRenderer
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.scene.Camera

class HealRenderer(renderManager: RenderManager) : EntityRenderer<Entity>(renderManager) {
    private val texture: Texture = renderManager.textureManager.provideTexture("gui/game.png")
    private val modelShader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)

    override fun renderEntityShape(entity: Entity, camera: Camera, x: Float, y: Float, z: Float) {
        modelShader.use<ModelShader> {
            GlUtils.glEnable(GlUtils.TEXTURE_2D)
            GlUtils.glEnable(GlUtils.BLEND)
            GlUtils.glBlendFuncDefault()
            GlUtils.glBindTexture(texture.glTexture)

            bindTexture(0)
            bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)

            val axisAlignedBB = entity.getBounds()
            val entityWidth = axisAlignedBB.maxX - axisAlignedBB.minX
            val entityHeight = axisAlignedBB.maxY - axisAlignedBB.minY
            val entityLength = axisAlignedBB.maxZ - axisAlignedBB.minZ

            val texX = 6
            val texY = 1
            val spriteWidth = texture.width / 8f
            val spriteHeight = texture.height / 8f

            Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.OPAQUE_MODEL) {
                // Front
                pos(-entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 0f, -1f)
                    .completeVertex()

                pos(entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 0f, -1f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 0f, -1f)
                    .completeVertex()

                pos(-entityWidth /2f, -entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 0f, -1f)
                    .completeVertex()

                // Back
                pos(entityWidth /2f, entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 0f, 1f)
                    .completeVertex()

                pos(-entityWidth /2f, entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 0f, 1f)
                    .completeVertex()

                pos(-entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 0f, 1f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 0f, 1f)
                    .completeVertex()

                // Left
                pos(-entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(-1f, 0f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(-1f, 0f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(-1f, 0f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(-1f, 0f, 0f)
                    .completeVertex()

                // Right
                pos(entityWidth /2f, entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(1f, 0f, 1f)
                    .completeVertex()

                pos(entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(1f, 0f, 1f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(1f, 0f, 1f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(1f, 0f, 1f)
                    .completeVertex()

                // Top
                pos(entityWidth /2f, entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 1f, 0f)
                    .completeVertex()

                pos(entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, 1f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 1f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, 1f, 0f)
                    .completeVertex()

                // Bottom
                pos(-entityWidth /2f, -entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, -1f, 0f)
                    .completeVertex()

                pos(-entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight + spriteHeight) / texture.height)
                    .norm(0f, -1f, 0f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, entityLength / 2f)
                    .tex((texX * spriteWidth + spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, -1f, 0f)
                    .completeVertex()

                pos(entityWidth /2f, -entityHeight / 2f, -entityLength / 2f)
                    .tex((texX * spriteWidth) / texture.width, (texY * spriteHeight) / texture.height)
                    .norm(0f, -1f, 0f)
                    .completeVertex()
            }

            GlUtils.glDisable(GlUtils.TEXTURE_2D)
            GlUtils.glDisable(GlUtils.BLEND)
        }
    }
}
