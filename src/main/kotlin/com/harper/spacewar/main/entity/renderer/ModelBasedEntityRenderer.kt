package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.model.Model
import com.harper.spacewar.main.resource.MeshesResources
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager

abstract class ModelBasedEntityRenderer<T : Entity>(renderManager: RenderManager) : EntityRenderer<T>(renderManager) {
    abstract val meshesResources: MeshesResources

    private val modelShader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)
    private var entityModel: Model? = null
    private var isLoaded = false

    override fun renderEntityShape(entity: T, camera: Camera, x: Float, y: Float, z: Float) {
        this.isLoaded = meshesResources.isLoaded()
        if (this.isLoaded) {
            if (this.entityModel != null) {
                modelShader.use<ModelShader> {
                    bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)
                    this@ModelBasedEntityRenderer.entityModel!!.render()
                }
            } else this.entityModel = Model(meshesResources.get())
        } else meshesResources.load()
    }
}