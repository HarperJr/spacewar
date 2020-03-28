package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.impl.EntityMissile
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.BloomShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.BloomShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager

class MissileRenderer(renderManager: RenderManager) : EntityRenderer<EntityMissile>(renderManager) {
    private val shader: BloomShader = ShaderProvider.provide(BloomShaderDefinition)

    override fun renderEntityShape(entity: EntityMissile, camera: Camera, x: Float, y: Float, z: Float) {
        val bounds = entity.getBounds()
        val missileWidth = (bounds.maxX - bounds.minX)
        val missileHeight = (bounds.maxY - bounds.minY)
        val missileLength = (bounds.maxZ - bounds.minZ)

        shader.use<BloomShader> {
            bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)
            bindColor(0xffff00ff)

            Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION) {
                pos(-missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()

                pos(+missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()

                pos(-missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()

                pos(+missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()

                pos(-missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, +missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, +missileHeight / 2f, -missileLength / 2f).completeVertex()

                pos(+missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()
                pos(+missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, -missileLength / 2f).completeVertex()
                pos(-missileWidth / 2f, -missileHeight / 2f, +missileLength / 2f).completeVertex()
            }
        }
    }
}