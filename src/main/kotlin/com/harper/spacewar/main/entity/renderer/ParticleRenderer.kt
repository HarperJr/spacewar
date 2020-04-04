package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.particle.EntityParticle
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.SpriteShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.SpriteShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.Matrix4f

class ParticleRenderer(renderManager: RenderManager) : EntityRenderer<EntityParticle>(renderManager) {
    private val shader: SpriteShader = ShaderProvider.provide(SpriteShaderDefinition)
    private val texture: Texture = renderManager.textureManager.provideTexture("gui/game.png")

    private val modelMatrix: Matrix4f = Matrix4f()

    override fun renderEntityShape(entity: EntityParticle, camera: Camera, x: Float, y: Float, z: Float) {
        shader.use<SpriteShader> {
            GlUtils.glEnable(GlUtils.TEXTURE_2D)
            GlUtils.glEnable(GlUtils.BLEND)
            GlUtils.glBlendFuncDefault()

            GlUtils.glBindTexture(texture.glTexture)
            bindTexture(0)

            bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)

            val spriteScale = entity.scale * 0.1f
            val particleWidth = texture.width / 8f
            val particleHeight = texture.height / 8f

            val particleType = entity.type
            val texX = particleType.texX * particleWidth
            val texY = particleType.texY * particleHeight

            Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX_COLOR) {
                pos(-particleWidth / 2f * spriteScale, particleHeight / 2f * spriteScale, 0f)
                    .tex(texX / texture.width, (texY + particleHeight) / texture.height)
                    .color(entity.color)
                    .completeVertex()

                pos(particleWidth / 2f * spriteScale, particleHeight / 2f * spriteScale, 0f)
                    .tex((texX + particleWidth) / texture.width, (texY + particleHeight) / texture.height)
                    .color(entity.color)
                    .completeVertex()

                pos(particleWidth / 2f * spriteScale, -particleHeight / 2f * spriteScale, 0f)
                    .tex((texX + particleWidth) / texture.width, texY / texture.height)
                    .color(entity.color)
                    .completeVertex()

                pos(-particleWidth / 2f * spriteScale, -particleHeight / 2f * spriteScale, 0f)
                    .tex(texX / texture.width, texY / texture.height)
                    .color(entity.color)
                    .completeVertex()
            }

            GlUtils.glDisable(GlUtils.TEXTURE_2D)
            GlUtils.glDisable(GlUtils.BLEND)
        }
    }
}