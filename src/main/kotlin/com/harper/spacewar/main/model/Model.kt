package com.harper.spacewar.main.model

import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.mesh.Mesh
import com.harper.spacewar.main.mesh.MeshRenderer

class Model(meshes: List<Mesh>) {
    private val innerMeshes: List<Mesh> = meshes
    private val renderers: Array<MeshRenderer> = Array(innerMeshes.size) { next ->
        MeshRenderer(innerMeshes[next])
    }

    fun render() {
        for (renderer in renderers)
            renderer.render(VertexFormat.OPAQUE_MODEL_UNTEXTURED)
    }
}