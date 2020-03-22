package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader
import org.joml.Matrix4f

class ModelShader(shaderProgram: Int) : Shader(shaderProgram) {
    override val uniforms: List<String> = emptyList()

    override fun bindAttributes() {
        attributeLocation(A_POSITION, "position")
        attributeLocation(A_TEX_COORD, "texCoord")
        attributeLocation(A_NORMALS, "normal")
    }

    companion object {
        const val A_POSITION = 0
        const val A_TEX_COORD = 1
        const val A_NORMALS = 2
    }
}