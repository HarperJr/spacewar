package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader

class GuiShader(shaderProgram: Int) : Shader(shaderProgram) {
    override val uniforms: List<String> = listOf(U_TEXTURE)

    override fun bindAttributes() {
        attributeLocation(A_POSITION, "position")
        attributeLocation(A_TEX_COORD, "texCoord")
    }

    fun texture(texture: Int) {
        uniformInt(U_TEXTURE, texture)
    }

    companion object {
        // Attributes
        const val A_POSITION = 0
        const val A_TEX_COORD = 1
        // Uniforms
        private const val U_TEXTURE = "tex"
    }
}
