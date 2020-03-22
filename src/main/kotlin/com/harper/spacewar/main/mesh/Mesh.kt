package com.harper.spacewar.main.mesh

import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.mesh.material.Material

/**
 * This class represents any mesh
 */
class Mesh(val name: String, val data: MeshData, val material: Material) {
    fun render(bufferBuilder: BufferBuilder) {
        val verticesCount = data.verticesCount
        for (i in 0 until verticesCount) {
            bufferBuilder.pos(data.vertices[i * 3 + 0], data.vertices[i * 3 + 1], data.vertices[i * 3 + 2])
                .also { bb ->
                    if (data.hasTextures) {
                        bb.tex(data.getTexCoords(0)[i * 2 + 0], data.getTexCoords(0)[i * 2 + 1])
                    }

                    if (data.hasNormals) {
                        bb.norm(data.normals[i * 3 + 0], data.normals[i * 3 + 1], data.normals[i * 3 + 2])
                    }
                }.completeVertex()
        }
    }
}