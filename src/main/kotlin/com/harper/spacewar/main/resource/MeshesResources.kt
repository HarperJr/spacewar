package com.harper.spacewar.main.resource

import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.mesh.Mesh
import com.harper.spacewar.main.mesh.loader.MeshLoader
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class MeshesResources(modelPath: String, textureManager: TextureManager) :
    Resource<List<Mesh>>(modelPath) {
    private val meshLoader: MeshLoader = MeshLoader(textureManager)

    override fun asyncLoad(): FutureTask<List<Mesh>> {
        return FutureTask(Callable<List<Mesh>> {
            meshLoader.load(super.path)
        })
    }
}