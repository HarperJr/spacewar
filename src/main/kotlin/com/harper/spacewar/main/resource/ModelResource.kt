package com.harper.spacewar.main.resource

import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.mesh.loader.MeshLoader
import com.harper.spacewar.main.model.Model
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class ModelResource(modelPath: String, textureManager: TextureManager) :
    Resource<Model>(modelPath) {
    private val meshLoader: MeshLoader = MeshLoader(textureManager)

    override fun asyncLoad(): FutureTask<Model> {
        return FutureTask(Callable<Model> {
            Model(meshLoader.load(super.path))
        })
    }
}