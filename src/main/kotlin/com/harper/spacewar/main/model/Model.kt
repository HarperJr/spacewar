package com.harper.spacewar.main.model

import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.mesh.Mesh
import com.harper.spacewar.main.mesh.MeshRenderer
import com.harper.spacewar.main.mesh.material.Material

class Model(private val innerMeshes: List<Mesh>) {
    val innerMeshCount: Int
        get() = innerMeshes.count()

    private val renderers: Array<MeshRenderer> = Array(innerMeshCount) { index ->
        MeshRenderer(this.innerMeshes[index])
    }

    fun getTexture(i: Int, type: Texture.Type): Material.MaterialTexture? {
        val material = this.innerMeshes[i].material
        return material.textures.find { it.type == type }
    }

    fun renderMesh(i: Int) {
        val material = this.innerMeshes[i].material
        val vertexFormat = if (material.hasTextures) {
            VertexFormat.OPAQUE_MODEL
        } else VertexFormat.OPAQUE_MODEL_UNTEXTURED
        renderers[i].render(vertexFormat)
    }
}