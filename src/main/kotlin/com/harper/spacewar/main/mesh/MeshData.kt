package com.harper.spacewar.main.mesh

import java.nio.IntBuffer

class MeshData(
    val verticesCount: Int,
    val vertices: FloatArray,
    val texCoords: List<FloatArray>,
    val normals: FloatArray,
    val indexes: IntBuffer
) {
    fun getTexCoords(index: Int): FloatArray {
        return texCoords[index]
    }

    val hasTextures: Boolean
        get() = texCoords.isNotEmpty()

    val hasNormals: Boolean
        get() = normals.isNotEmpty()
}