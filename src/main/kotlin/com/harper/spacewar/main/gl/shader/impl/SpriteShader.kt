package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader

class SpriteShader(shaderProgram: Int) : Shader(shaderProgram) {
    override val uniforms: List<String> = listOf(U_TEXTURE)

    override fun bindAttributes() {
        attributeLocation(Shader.A_POSITION, "position")
        attributeLocation(Shader.A_UV, "uv")
    }

    fun bindTexture(texture: Int) {
        uniformInt(U_TEXTURE, texture)
    }

    companion object {
        // Uniforms
        private const val U_TEXTURE = "tex"
    }
}
