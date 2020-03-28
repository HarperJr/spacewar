package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader

class ModelShader(shaderProgram: Int) : Shader(shaderProgram) {
    override val uniforms: List<String> = emptyList()

    override fun bindAttributes() {
        attributeLocation(Shader.A_POSITION, "position")
        attributeLocation(Shader.A_UV, "uv")
        attributeLocation(Shader.A_NORMALS, "normal")
    }
}