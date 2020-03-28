package com.harper.spacewar.main.sprite.renderer

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.shader.impl.SpriteShader
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.sprite.Sprite
import org.joml.Matrix4f

abstract class SpriteRenderer<T : Sprite> {
    protected abstract val shader: SpriteShader
    protected abstract val texture: Texture

    private val modelMatrix: Matrix4f = Matrix4f()

    protected abstract fun renderSprite(sprite: T, camera: Camera, x: Float, y: Float, z: Float)

    fun render(sprite: T, camera: Camera, x: Float, y: Float, z: Float) {
        shader.use<SpriteShader> {
            GlUtils.glEnable(GlUtils.TEXTURE_2D)
            GlUtils.glEnable(GlUtils.BLEND)
            GlUtils.glBlendFuncDefault()

            bindMatrices(getModelMatrixForSprite(sprite, x, y, z), camera.view, camera.projection)
            bindTexture(0)

            GlUtils.glActiveTexture(GlUtils.TEXTURE_0)
            GlUtils.glBindTexture(texture.glTexture)

            renderSprite(sprite, camera, x, y, z)

            GlUtils.glDisable(GlUtils.BLEND)
            GlUtils.glDisable(GlUtils.TEXTURE_2D)
        }
    }

    private fun getModelMatrixForSprite(sprite: Sprite, x: Float, y: Float, z: Float): Matrix4f {
        modelMatrix.identity()
        return modelMatrix.apply {
            translate(x, y, z)
            rotate(sprite.rotation)
        }
    }
}