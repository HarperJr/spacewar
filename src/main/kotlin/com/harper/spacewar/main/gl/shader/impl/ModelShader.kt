package com.harper.spacewar.main.gl.shader.impl

import com.harper.spacewar.main.gl.shader.Shader

class ModelShader(shaderProgram: Int) : Shader(shaderProgram) {
    private val textures = arrayOf(U_TEXTURE_AMBIENT, U_TEXTURE_DIFFUSE, U_TEXTURE_SPECULAR, U_TEXTURE_EMISSIVE)

    override val uniforms: List<String> = listOf(*textures)

    override fun bindAttributes() {
        attributeLocation(Shader.A_POSITION, "position")
        attributeLocation(Shader.A_UV, "uv")
        attributeLocation(Shader.A_NORMALS, "normal")
    }

    fun bindTexture(texture: Int) {
        uniformInt(textures[texture], texture)
    }

    companion object {
        private const val U_TEXTURE_AMBIENT = "texAmbient"
        private const val U_TEXTURE_DIFFUSE = "texDiffuse"
        private const val U_TEXTURE_SPECULAR = "texSpecular"
        private const val U_TEXTURE_EMISSIVE = "texEmissive"
    }
}