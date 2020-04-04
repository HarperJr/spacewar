package com.harper.spacewar.main.entity.sprite.renderer

import com.harper.spacewar.main.entity.renderer.EntityRenderer
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.shader.impl.SpriteShader
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.entity.sprite.EntitySprite
import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.Matrix4f

abstract class SpriteRenderer(renderManager: RenderManager) : EntityRenderer<EntitySprite>(renderManager) {
    protected abstract val shader: SpriteShader
    protected abstract val texture: Texture

    private val modelMatrix: Matrix4f = Matrix4f()

    protected abstract fun renderSprite(sprite: EntitySprite, camera: Camera, x: Float, y: Float, z: Float)

    override fun renderEntityShape(entity: EntitySprite, camera: Camera, x: Float, y: Float, z: Float) {
        shader.use<SpriteShader> {
            GlUtils.glEnable(GlUtils.TEXTURE_2D)
            GlUtils.glEnable(GlUtils.BLEND)
            GlUtils.glBlendFuncDefault()

            bindMatrices(getModelMatrixForSprite(entity, x, y, z), camera.view, camera.projection)
            bindTexture(0)

            GlUtils.glActiveTexture(GlUtils.TEXTURE_0)
            GlUtils.glBindTexture(texture.glTexture)

            renderSprite(entity, camera, x, y, z)

            GlUtils.glDisable(GlUtils.BLEND)
            GlUtils.glDisable(GlUtils.TEXTURE_2D)
        }
    }

    private fun getModelMatrixForSprite(entitySprite: EntitySprite, x: Float, y: Float, z: Float): Matrix4f {
        modelMatrix.identity()
        return modelMatrix.apply {
            translate(x, y, z)
            rotate(entitySprite.rotation)
        }
    }
}