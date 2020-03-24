package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.definition.OutlineShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.impl.OutlineShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.model.Model
import com.harper.spacewar.main.resource.MeshesResources
import org.joml.Matrix4f

abstract class EntityRenderer<T : Entity>(private val spacewar: Spacewar) {
    abstract val meshesResources: MeshesResources

    private val modelShader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)
    private val outlineShader: OutlineShader = ShaderProvider.provide(OutlineShaderDefinition)

    private val modelMatrix: Matrix4f = Matrix4f()
    private val identityMatrix: Matrix4f = Matrix4f()

    private var entityModel: Model? = null
    private var isLoaded = false

    fun render(entity: T, camera: Camera, x: Float, y: Float, z: Float) {
        this.isLoaded = meshesResources.isLoaded()
        if (this.isLoaded) {
            if (this.entityModel != null) {
                GlUtils.glEnable(GlUtils.DEPTH_TEST)
                GlUtils.glDepthFunc(GlUtils.DEPTH_LESS)
                GlUtils.glEnableDepthMask()

                // Draw model pass
                modelShader.use<ModelShader> {
                    bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)
                    this@EntityRenderer.entityModel!!.render()
                }

                // Draw AABB pass
                outlineShader.use<OutlineShader> {
                    bindColor(0xff0000ff)
                    bindMatrices(identityMatrix, camera.view, camera.projection)
                    entity.drawAxisAlignedBox()
                }

                GlUtils.glDisable(GlUtils.DEPTH_TEST)
            } else this.entityModel = Model(meshesResources.get())
        } else meshesResources.load()
    }

    private fun getModelMatrixForEntity(entity: T, x: Float, y: Float, z: Float): Matrix4f {
        this.modelMatrix.identity()
        return modelMatrix.apply {
            translate(x, y, z)
            rotateY(entity.rotYaw / 180f * Math.PI.toFloat())
            rotateX(entity.rotPitch / 180f * Math.PI.toFloat())
        }
    }
}
