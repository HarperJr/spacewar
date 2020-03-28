package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.definition.OutlineShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.impl.OutlineShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.Matrix4f

abstract class EntityRenderer<T : Entity>(private val renderManager: RenderManager) {
    private val modelShader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)
    private val outlineShader: OutlineShader = ShaderProvider.provide(OutlineShaderDefinition)

    private val modelMatrix: Matrix4f = Matrix4f()
    private val identityMatrix: Matrix4f = Matrix4f()

    abstract fun renderEntityShape(entity: T, camera: Camera, x: Float, y: Float, z: Float)

    fun render(entity: T, camera: Camera, x: Float, y: Float, z: Float) {
        GlUtils.glEnable(GlUtils.DEPTH_TEST)
        GlUtils.glDepthFunc(GlUtils.DEPTH_LESS)
        GlUtils.glEnableDepthMask()

        // Draw model pass
        renderEntityShape(entity, camera, x, y, z)

        // Draw AABB pass
        outlineShader.use<OutlineShader> {
            bindColor(0xff0000ff)
            bindMatrices(identityMatrix, camera.view, camera.projection)
            drawAxisAlignedBox(entity)
        }

        for (sprite in entity.sprites)
            renderManager.renderSprite(
                sprite,
                camera,
                x + sprite.position.x,
                y + sprite.position.y,
                z + sprite.position.z
            )

        GlUtils.glDisable(GlUtils.DEPTH_TEST)
    }

    protected fun getModelMatrixForEntity(entity: T, x: Float, y: Float, z: Float): Matrix4f {
        this.modelMatrix.identity()
        return modelMatrix.apply {
            translate(x, y, z)
            rotate(entity.rotation)
        }
    }

    private fun drawAxisAlignedBox(entity: T) {
        val axisAlignedBox = entity.getBounds()
        GlUtils.glColor(0xff0000fff)
        GlUtils.glLineWidth(2f)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glPopMatrix()
        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_LINES, VertexFormat.POSITION) {
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()
        }
        GlUtils.glPushMatrix()
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
    }
}
