package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.model.Model
import com.harper.spacewar.main.resource.MeshesResources
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager

abstract class ModelBasedEntityRenderer<T : Entity>(renderManager: RenderManager) : EntityRenderer<T>(renderManager) {
    abstract val meshesResources: MeshesResources

    private val modelShader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)
    private val textureManager: TextureManager = renderManager.textureManager
    private var entityModel: Model? = null
    private var isLoaded = false

    override fun renderEntityShape(entity: T, camera: Camera, x: Float, y: Float, z: Float) {
        this.isLoaded = meshesResources.isLoaded()
        if (this.isLoaded) {
            if (this.entityModel != null) {
                val model = this@ModelBasedEntityRenderer.entityModel!!
                modelShader.use<ModelShader> {
                    GlUtils.glEnable(GlUtils.TEXTURE_2D)

                    bindMatrices(getModelMatrixForEntity(entity, x, y, z), camera.view, camera.projection)
                    for (i in 0 until model.innerMeshCount) {
                        bindTextures(this, model, i)
                        model.renderMesh(i)
                    }

                    GlUtils.glDisable(GlUtils.TEXTURE_2D)
                }
            } else this.entityModel = Model(meshesResources.get())
        } else meshesResources.load()
    }

    private fun bindTextures(shader: ModelShader, model: Model, index: Int) {
        for ((i, type) in Texture.Type.values().withIndex()) {
            val matTexture = model.getTexture(index, type)
            if (matTexture != null) {
                val texture = this.textureManager.provideTexture(matTexture.path)
                GlUtils.glBindTexture(texture.glTexture)
                //GlUtils.glActiveTexture(GlUtils.TEXTURE_0 + i)
                shader.bindTexture(i)
            }
        }
    }
}