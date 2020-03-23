package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader
import org.joml.Vector4f

class OutlineShader(shaderProgram: Int) : Shader(shaderProgram) {
    override val uniforms: List<String>
        get() = listOf(U_COLOR)

    override fun bindAttributes() {
        attributeLocation(ModelShader.A_POSITION, "position")
    }

    fun bindColor(color: Long) {
        uniformVec4(U_COLOR, Vector4f((color shr 24 and 255) / 255f, (color shr 16 and 255) / 255f, (color shr 8 and 255) / 255f, (color and 255) / 255f))
    }

    companion object {
        private const val U_COLOR = "color"
    }
}